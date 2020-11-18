/*
 * Copyright (c) 2020 Proton Technologies AG
 * This file is part of Proton Technologies AG and ProtonCore.
 *
 * ProtonCore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonCore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.proton.core.auth.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.proton.core.auth.domain.crypto.CryptoProvider
import me.proton.core.auth.domain.entity.AddressKey
import me.proton.core.auth.domain.entity.KeySecurity
import me.proton.core.auth.domain.entity.KeyType
import me.proton.core.auth.domain.entity.SignedKeyList
import me.proton.core.auth.domain.entity.User
import me.proton.core.auth.domain.repository.AuthRepository
import me.proton.core.domain.arch.DataResult
import me.proton.core.domain.arch.onFailure
import me.proton.core.domain.arch.onSuccess
import me.proton.core.network.domain.session.SessionId
import javax.inject.Inject

/**
 * @author Dino Kadrikj.
 */
class UpdateUsernameOnlyAccount @Inject constructor(
    private val authRepository: AuthRepository,
    private val cryptoProvider: CryptoProvider
) {

    /**
     * State sealed class with various (success, error) outcome state subclasses.
     */
    sealed class State {
        object Processing : State()
        data class Success(val user: User) : State()
        sealed class Error : State() {
            data class Message(val message: String?) : Error()
            object EmptyCredentials : Error()
            object EmptyDomain : Error()
            data class GeneratingPrivateKeyFailed(val message: String? = null) : Error()
            data class GeneratingSignedKeyListFailed(val message: String? = null) : Error()
        }
    }

    @Suppress("TooGenericExceptionCaught")
    operator fun invoke(
        sessionId: SessionId,
        domain: String,
        username: String,
        passphrase: ByteArray
    ): Flow<State> = flow {
        if (username.isEmpty() || passphrase.isEmpty()) {
            emit(State.Error.EmptyCredentials)
            return@flow
        }
        if (domain.isEmpty()) {
            emit(State.Error.EmptyDomain)
            return@flow
        }
        emit(State.Processing)
        // step 1. create address
        val createAddressResult = authRepository.createAddress(sessionId, domain, username)
        createAddressResult.onFailure { message, _, _ ->
            emit(State.Error.Message(message))
            return@flow
        }
        val address = (createAddressResult as DataResult.Success).value

        // step 2. fetch a random modulus
        val randomModulusResult = authRepository.randomModulus()
        randomModulusResult.onFailure { message, _, _ ->
            emit(State.Error.Message(message))
            return@flow
        }
        val modulus = (randomModulusResult as DataResult.Success).value
        // step 3. generate new private key and signed key list
        val randomSalt = cryptoProvider.createNewKeySalt()
        val generatedPassphrase = cryptoProvider.generatePassphrase(passphrase, randomSalt)
        val privateKey = try {
            cryptoProvider.generateNewPrivateKey(username, domain, generatedPassphrase, KeyType.RSA, KeySecurity.HIGH)
        } catch (privateKeyException: Exception) { // gopenpgp library throws generic exception
            emit(State.Error.GeneratingPrivateKeyFailed(privateKeyException.message))
            return@flow
        }

        val signedKeyList = try {
            cryptoProvider.generateSignedKeyList(privateKey, generatedPassphrase)
        } catch (signedKeyListException: Exception) { // gopenpgp library throws generic exception
            emit(State.Error.GeneratingSignedKeyListFailed(signedKeyListException.message))
            return@flow
        }
        // step 4. setup address key
        authRepository.setupAddressKeys(
            sessionId = sessionId,
            primaryKey = privateKey,
            keySalt = randomSalt,
            addressKeyList = listOf(
                AddressKey(address.id, privateKey, SignedKeyList(signedKeyList.first, signedKeyList.second))
            ),
            auth = cryptoProvider.calculatePasswordVerifier(
                username = username,
                passphrase = generatedPassphrase,
                modulusId = modulus.modulusId,
                modulus = modulus.modulus
            )
        ).onFailure { message, _, _ ->
            emit(State.Error.Message(message))
        }.onSuccess {
            emit(State.Success(it.copy(passphrase = generatedPassphrase)))
        }
    }
}
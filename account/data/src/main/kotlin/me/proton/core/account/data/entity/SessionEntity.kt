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

package me.proton.core.account.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import me.proton.core.data.db.CommonConverters
import me.proton.core.data.crypto.EncryptedString
import me.proton.core.data.crypto.StringCrypto
import me.proton.core.data.crypto.decrypt
import me.proton.core.domain.entity.Product
import me.proton.core.network.domain.humanverification.HumanVerificationHeaders
import me.proton.core.network.domain.session.Session
import me.proton.core.network.domain.session.SessionId

@Entity(
    primaryKeys = ["sessionId"],
    indices = [
        Index("sessionId"),
        Index("userId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SessionEntity(
    val userId: String,
    val sessionId: String,
    val accessToken: EncryptedString,
    val refreshToken: EncryptedString,
    val humanHeaderTokenType: EncryptedString?,
    val humanHeaderTokenCode: EncryptedString?,
    val scopes: String,
    val product: Product
) {
    fun toSession(crypto: StringCrypto): Session = Session(
        sessionId = SessionId(sessionId),
        accessToken = accessToken.decrypt(crypto),
        refreshToken = refreshToken.decrypt(crypto),
        headers = humanHeaderTokenType?.let { tokenType ->
            humanHeaderTokenCode?.let { tokenCode ->
                HumanVerificationHeaders(
                    tokenType = tokenType.decrypt(crypto),
                    tokenCode = tokenCode.decrypt(crypto)
                )
            }
        },
        scopes = CommonConverters.fromStringToListOfString(scopes).orEmpty()
    )
}
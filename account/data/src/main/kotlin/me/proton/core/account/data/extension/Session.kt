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

package me.proton.core.account.data.extension

import me.proton.core.account.data.entity.SessionEntity
import me.proton.core.data.db.CommonConverters
import me.proton.core.data.crypto.StringCrypto
import me.proton.core.data.crypto.encrypt
import me.proton.core.domain.entity.Product
import me.proton.core.domain.entity.UserId
import me.proton.core.network.domain.session.Session

fun Session.toSessionEntity(
    userId: UserId,
    product: Product,
    crypto: StringCrypto
): SessionEntity = SessionEntity(
    userId = userId.id,
    sessionId = sessionId.id,
    accessToken = accessToken.encrypt(crypto),
    refreshToken = refreshToken.encrypt(crypto),
    humanHeaderTokenType = headers?.tokenType?.encrypt(crypto),
    humanHeaderTokenCode = headers?.tokenCode?.encrypt(crypto),
    scopes = CommonConverters.fromListOfStringToString(scopes).orEmpty(),
    product = product
)
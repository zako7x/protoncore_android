/*
 * Copyright (c) 2021 Proton Technologies AG
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

package me.proton.core.test.android.robots.payments

import android.widget.EditText
import me.proton.core.payment.presentation.R
import me.proton.core.test.android.robots.other.CountryRobot

/**
 * [AddCreditCardRobot] class contains new credit card addition actions and verifications implementation
 */
class AddCreditCardRobot : PaymentRobot() {

    /**
     * Fills in credit card holder name
     * @return [AddCreditCardRobot]
     */
    fun ccname(name: String): AddCreditCardRobot = setText(R.id.cardNameInput, name)

    /**
     * Fills in credit card number
     * @return [AddCreditCardRobot]
     */
    fun ccnumber(number: String): AddCreditCardRobot = setText(R.id.cardNumberInput, number)

    /**
     * Fills in credit card expiry date
     * @return [AddCreditCardRobot]
     */
    fun expirationDate(date: String): AddCreditCardRobot = setText(R.id.expirationDateInput, date)

    /**
     * Fills in credit card security number
     * @return [AddCreditCardRobot]
     */
    fun cvc(securityCode: String): AddCreditCardRobot = setText(R.id.cvcInput, securityCode)

    /**
     * Fills in credit card holder postal code
     * @return [AddCreditCardRobot]
     */
    fun postalCode(number: String): AddCreditCardRobot {
        view.withId(R.id.scrollContent).swipeUp()
        return setText(R.id.postalCodeInput, number)
    }

    /**
     * Clicks country selection element
     * @return [CountryRobot]
     */
    fun country(): CountryRobot = clickElement(R.id.countriesText, EditText::class.java)
}
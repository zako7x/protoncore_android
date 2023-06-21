/*
 * Copyright (c) 2023 Proton AG
 * This file is part of Proton AG and ProtonCore.
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

package me.proton.core.accountrecovery.presentation.compose.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import me.proton.core.accountrecovery.presentation.compose.R
import me.proton.core.accountrecovery.presentation.compose.dialog.AccountRecoveryDialog
import me.proton.core.accountrecovery.presentation.compose.entity.AccountRecoveryDialogInput
import me.proton.core.compose.theme.ProtonTheme
import me.proton.core.domain.entity.UserId
import me.proton.core.presentation.ui.ProtonActivity
import me.proton.core.presentation.utils.errorToast
import me.proton.core.presentation.utils.getUserMessage

@AndroidEntryPoint
class AccountRecoveryDialogActivity : ProtonActivity() {

    private val input: AccountRecoveryDialogInput by lazy {
        requireNotNull(intent?.extras?.getParcelable(ARG_INPUT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProtonTheme {
                AccountRecoveryDialog(
                    userId = UserId(input.userId),
                    onClosed = { setResultAndFinish() },
                    onError = {
                        errorToast(
                            it?.getUserMessage(resources)
                                ?: getString(R.string.presentation_error_general)
                        )
                        setResultAndFinish()
                    }
                )
            }
        }
    }

    private fun setResultAndFinish() {
        setResult(RESULT_OK, Intent())
        finish()
    }

    companion object {

        const val ARG_INPUT = "arg.accountRecoveryDialogInput"

        fun start(context: Context, input: AccountRecoveryDialogInput) {
            context.startActivity(getIntent(context, input))
        }

        fun getIntent(context: Context, input: AccountRecoveryDialogInput): Intent =
            Intent(context, AccountRecoveryDialogActivity::class.java).apply {
                putExtra(ARG_INPUT, input)
            }
    }
}
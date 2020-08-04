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

package me.proton.android.core.presentation.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import me.proton.android.core.presentation.R

/**
 * Base Proton Fragment from which all project fragments should extend.
 *
 * @author Dino Kadrikj.
 */
abstract class ProtonDialogFragment<DB : ViewDataBinding> : DialogFragment() {

    private var internalBinding: DB? = null
    protected val binding: DB
        get() = internalBinding
            ?: throw IllegalStateException("Accessing binding outside of lifecycle")

    protected abstract fun layoutId(): Int
    protected abstract fun getStyleResource(): Int
    protected abstract fun onBackPressed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, getStyleResource())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        internalBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        binding.setLifecycleOwner { lifecycle }
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = object : Dialog(requireContext(), theme) {
            override fun onBackPressed() {
                this@ProtonDialogFragment.onBackPressed()
            }
        }
        // request a window without the title
        dialog.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        }
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        internalBinding = null
    }
}
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

package me.proton.android.core.presentation.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.proton.android.core.presentation.R
import me.proton.android.core.presentation.ui.isInputTypePassword
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Custom input view attributes tests.
 * @author Dino Kadrikj.
 */
@RunWith(RobolectricTestRunner::class)
class ProtonInputAttributesTest {

    private lateinit var protonInput: ProtonInput
    private lateinit var activity: AppCompatActivity
    private lateinit var parent: FrameLayout

    @Before
    fun beforeEveryTest() {
        val activityController = Robolectric.buildActivity(AppCompatActivity::class.java)
        activity = activityController.get()
        parent = FrameLayout(activity)
        activity.windowManager.addView(parent, WindowManager.LayoutParams(500, 500))
    }

    @Test
    fun `label attribute inits correct`() {
        val attributes = Robolectric.buildAttributeSet()
        attributes.addAttribute(R.attr.label, "test label")

        protonInput = LayoutInflater.from(activity)
            .inflate(R.layout.proton_input, ProtonInput(activity, attributes.build())) as ProtonInput

        parent.addView(protonInput)

        val labelView = protonInput.findViewById<TextView>(R.id.label)
        assertEquals(View.VISIBLE, labelView.visibility)
        assertEquals("test label", labelView.text.toString())
    }

    @Test
    fun `assistive text attribute inits correct`() {
        val attributes = Robolectric.buildAttributeSet()
        attributes.addAttribute(R.attr.assistiveText, "test assistive text")

        protonInput = LayoutInflater.from(activity)
            .inflate(R.layout.proton_input, ProtonInput(activity, attributes.build())) as ProtonInput

        parent.addView(protonInput)

        val assistiveTextView = protonInput.findViewById<TextView>(R.id.assistiveText)
        assertEquals(View.VISIBLE, assistiveTextView.visibility)
        assertEquals("test assistive text", assistiveTextView.text.toString())
    }

    @Test
    fun `text attribute inits correct`() {
        val attributes = Robolectric.buildAttributeSet()
        attributes.addAttribute(android.R.attr.text, "test input text")

        protonInput = LayoutInflater.from(activity)
            .inflate(R.layout.proton_input, ProtonInput(activity, attributes.build())) as ProtonInput

        parent.addView(protonInput)
        val inputViewParent = protonInput.getChildAt(1) as FrameLayout
        val inputView = inputViewParent.getChildAt(0) as EditText
        assertEquals("test input text", inputView.text.toString())
        assertEquals("", inputView.hint.toString())
    }

    @Test
    fun `hint attribute inits correct`() {
        val attributes = Robolectric.buildAttributeSet()
        attributes.addAttribute(android.R.attr.hint, "test hint text")

        protonInput = LayoutInflater.from(activity)
            .inflate(R.layout.proton_input, ProtonInput(activity, attributes.build())) as ProtonInput

        parent.addView(protonInput)
        val inputViewParent = protonInput.getChildAt(1) as FrameLayout
        val inputView = inputViewParent.getChildAt(0) as EditText
        assertEquals("test hint text", inputView.hint.toString())
    }

    @Test
    fun `inputType attribute inits correct`() {
        val attributes = Robolectric.buildAttributeSet()
        attributes.addAttribute(android.R.attr.inputType, "textPassword")

        protonInput = LayoutInflater.from(activity)
            .inflate(R.layout.proton_input, ProtonInput(activity, attributes.build())) as ProtonInput

        parent.addView(protonInput)
        val inputViewParent = protonInput.getChildAt(1) as FrameLayout
        val inputView = inputViewParent.getChildAt(0) as EditText
        assertTrue(inputView.inputType.isInputTypePassword())
    }

    @Test
    fun `enabled attribute not set input view is enabled by default`() {
        val attributes = Robolectric.buildAttributeSet()

        protonInput = LayoutInflater.from(activity)
            .inflate(R.layout.proton_input, ProtonInput(activity, attributes.build())) as ProtonInput

        parent.addView(protonInput)
        val inputViewParent = protonInput.getChildAt(1) as FrameLayout
        val inputView = inputViewParent.getChildAt(0) as EditText
        assertTrue(inputView.isEnabled)
    }

    @Test
    fun `enabled attribute set FALSE input view is disabled`() {
        val attributes = Robolectric.buildAttributeSet()
        attributes.addAttribute(android.R.attr.enabled, "false")
        protonInput = LayoutInflater.from(activity)
            .inflate(R.layout.proton_input, ProtonInput(activity, attributes.build())) as ProtonInput

        parent.addView(protonInput)
        val inputViewParent = protonInput.getChildAt(1) as FrameLayout
        val inputView = inputViewParent.getChildAt(0) as EditText
        assertFalse(inputView.isEnabled)
    }
}

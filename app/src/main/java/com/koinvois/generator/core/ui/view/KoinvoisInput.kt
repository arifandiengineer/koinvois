package com.koinvois.generator.core.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.koinvois.generator.R

/**
 * Reusable Input Component following MD3 standards.
 * Enforces 14sp text size and consistent padding.
 */
class KoinvoisInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.textInputStyle
) : TextInputLayout(context, attrs, defStyleAttr) {

    private val editText: TextInputEditText

    init {
        // Apply our custom MD3 style by default
        LayoutInflater.from(context).inflate(R.layout.view_koinvois_input, this, true)
        editText = findViewById(R.id.editText)

        context.obtainStyledAttributes(attrs, R.styleable.KoinvoisInput).apply {
            val hint = getString(R.styleable.KoinvoisInput_android_hint)
            val inputType = getInt(R.styleable.KoinvoisInput_android_inputType, -1)
            val iconRes = getResourceId(R.styleable.KoinvoisInput_inputIcon, 0)

            setHint(hint)
            if (inputType != -1) editText.inputType = inputType
            if (iconRes != 0) setStartIconDrawable(iconRes)

            recycle()
        }
    }

    fun getText(): String = editText.text.toString()
    fun setText(text: String?) = editText.setText(text ?: "")
    fun setText(text: Int) = editText.setText(text.toString())

    fun getString(): String? {
        val str = editText.text.toString()
        return if (str.isEmpty()) null else str
    }
}

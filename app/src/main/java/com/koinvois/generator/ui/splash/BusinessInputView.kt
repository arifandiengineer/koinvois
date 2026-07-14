package com.koinvois.generator.ui.splash

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.koinvois.generator.R
import com.koinvois.generator.databinding.ViewBusinessInputBinding

class BusinessInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewBusinessInputBinding

    init {
        binding = ViewBusinessInputBinding.inflate(LayoutInflater.from(context), this)
        
        context.obtainStyledAttributes(attrs, R.styleable.BusinessInputView).apply {
            val icon = getResourceId(R.styleable.BusinessInputView_inputIcon, 0)
            val iconTint = getColor(R.styleable.BusinessInputView_inputIconTint, 0)
            val label = getString(R.styleable.BusinessInputView_inputLabel)
            val hint = getString(R.styleable.BusinessInputView_inputHint)
            val inputType = getInt(R.styleable.BusinessInputView_android_inputType, 1) // default text

            if (icon != 0) binding.imgIcon.setImageResource(icon)
            if (iconTint != 0) binding.imgIcon.imageTintList = android.content.res.ColorStateList.valueOf(iconTint)
            binding.txtLabel.text = label
            binding.editText.hint = hint
            binding.editText.inputType = inputType

            recycle()
        }
    }

    fun getText(): String = binding.editText.text.toString()
    fun setText(text: String) = binding.editText.setText(text)
    fun getEditText() = binding.editText
}

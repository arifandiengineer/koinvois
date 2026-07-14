package com.koinvois.generator.core.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.koinvois.generator.R

/**
 * Reusable Label Component with Icon support.
 */
class KoinvoisLabel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val imgIcon: ImageView
    private val txtLabel: TextView

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.view_koinvois_label, this, true)
        imgIcon = findViewById(R.id.imgIcon)
        txtLabel = findViewById(R.id.txtLabel)

        context.obtainStyledAttributes(attrs, R.styleable.KoinvoisLabel).apply {
            val text = getString(R.styleable.KoinvoisLabel_android_text)
            val iconRes = getResourceId(R.styleable.KoinvoisLabel_labelIcon, 0)

            txtLabel.text = text
            if (iconRes != 0) {
                imgIcon.setImageResource(iconRes)
                imgIcon.visibility = VISIBLE
            } else {
                imgIcon.visibility = GONE
            }

            recycle()
        }
    }

    fun setText(text: String) {
        txtLabel.text = text
    }
}

package com.koinvois.generator.utilities.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.koinvois.generator.R
import com.koinvois.generator.utilities.SafeClickListener
import java.util.regex.Pattern

fun View.hide() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

/** MD3 success Snackbar — replaces Toast for save/update/delete success feedback. */
fun View.showSuccessSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(ContextCompat.getColor(context, R.color.color_success))
        .setTextColor(ContextCompat.getColor(context, R.color.md_theme_onSuccess))
        .show()
}

/** MD3 error Snackbar — replaces Toast for validation errors. */
fun View.showErrorSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(ContextCompat.getColor(context, R.color.color_error))
        .setTextColor(ContextCompat.getColor(context, R.color.md_theme_onError))
        .show()
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}


fun DialogFragment.setWidthPercent(percentage: Int) {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percent
    dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}

/**
 * Call this method (in onActivityCreated or later)
 * to make the dialog near-full screen.
 */
fun DialogFragment.setFullScreen() {
    dialog?.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

fun Context.getScreenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

fun ViewPager2.reduceDragSensitivity(f: Int = 4) {
    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
    recyclerViewField.isAccessible = true
    val recyclerView = recyclerViewField.get(this) as RecyclerView

    val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
    touchSlopField.isAccessible = true
    val touchSlop = touchSlopField.get(recyclerView) as Int
    touchSlopField.set(recyclerView, touchSlop * f)       // "8" was obtained experimentally
}

fun EditText.getString(): String? {
    return if (this.text.toString() == "") {
        null
    } else {
        this.text?.toString()
    }
}

fun EditText.getInt(): Int? {
    return if (this.text?.toString() != "") {
        this.text?.toString()?.toInt()
    } else {
        null
    }
}

fun EditText.getFloat(): Float? {
    return if (this.text?.toString() != "") {
        this.text?.toString()?.toFloat()
    } else {
        null
    }
}

fun TextView.getStringText(): String? {
    return if (this.text.isEmpty()) {
        null
    } else {
        this.text?.toString()
    }
}

fun TextView.getIntText(): Int? {
    return if (this.text.isEmpty()) {
        this.text?.toString()?.toInt()
    } else {
        null
    }
}

fun TextView.getFloatText(): Float? {
    return if (this.text.isEmpty()) {
        null
    } else {
        this.text?.toString()?.toFloat()
    }
}

fun Context?.toastL(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

private val BUTTON_ORIGINAL_TEXT_TAG_KEY = "koinvois_button_original_text".hashCode()

/**
 * Toggles a MD3 button between its normal state and a disabled, spinning-icon
 * loading state. No new View is created — the button's own [MaterialButton.icon]
 * slot hosts the spinner so this works with any existing Widget.App.Button* style.
 */
fun MaterialButton.setLoading(isLoading: Boolean, loadingText: String? = null) {
    if (isLoading) {
        if (getTag(BUTTON_ORIGINAL_TEXT_TAG_KEY) == null) {
            setTag(BUTTON_ORIGINAL_TEXT_TAG_KEY, text)
        }
        isEnabled = false
        text = loadingText ?: ""
        icon = ContextCompat.getDrawable(context, R.drawable.anim_button_spinner)
        iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
        (icon as? Animatable)?.start()
    } else {
        (icon as? Animatable)?.stop()
        icon = null
        isEnabled = true
        (getTag(BUTTON_ORIGINAL_TEXT_TAG_KEY) as? CharSequence)?.let { text = it }
    }
}

fun EditText.isEmail(): Boolean {
    val email = this.text.toString().trim()

    return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches()
}
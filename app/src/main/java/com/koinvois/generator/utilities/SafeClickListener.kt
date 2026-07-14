package com.koinvois.generator.utilities

import android.os.SystemClock
import android.view.View
import com.koinvois.generator.utilities.constants.Constants.DOUBLE_CLICK_DELAY

class SafeClickListener(
    private var defaultInterval: Int = DOUBLE_CLICK_DELAY,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}
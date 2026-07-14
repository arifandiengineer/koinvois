package com.koinvois.generator.utilities.signature

import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener

object ViewTreeObserverCompat {
    /**
     * Remove a previously installed global layout callback.
     * @param observer the view observer
     * @param victim the victim
     */
    fun removeOnGlobalLayoutListener(
        observer: ViewTreeObserver,
        victim: OnGlobalLayoutListener?
    ) {
        observer.removeOnGlobalLayoutListener(victim)
    }
}
package com.koinvois.generator.core.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Collects [flow] only while the fragment's view is at least [minActiveState],
 * cancelling/restarting automatically across the lifecycle — the standard
 * replacement for manual LiveData observation when a ViewModel exposes
 * StateFlow/SharedFlow.
 */
fun <T> Fragment.collectLatestOnLifecycle(
    flow: Flow<T>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    action: suspend (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(minActiveState) {
            flow.collectLatest(action)
        }
    }
}

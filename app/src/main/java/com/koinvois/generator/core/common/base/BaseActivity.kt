package com.koinvois.generator.core.common.base

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding

/**
 * ViewBinding + edge-to-edge scaffolding shared by every Activity.
 * Activities only wire up views/observers here — no business logic.
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    val binding: VB
        get() = requireNotNull(_binding) { "Binding accessed outside onCreate()/onDestroy() window" }

    protected abstract fun inflateBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = inflateBinding()
        setContentView(binding.root)
        applyDefaultSystemBarPadding()
        setupView()
        setupObservers()
    }

    /**
     * Sprint 1 edge-to-edge baseline: pad the root view by the system bar
     * insets so existing screens keep their current layout instead of
     * drawing under the status/navigation bar. A screen can opt out by
     * overriding this to apply insets more surgically once it gets its own
     * design pass (e.g. drawing a toolbar behind a translucent status bar).
     */
    protected open fun applyDefaultSystemBarPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(left = bars.left, top = bars.top, right = bars.right, bottom = bars.bottom)
            insets
        }
    }

    /** Wire click listeners, adapters, navigation — no data loading. */
    protected open fun setupView() = Unit

    /** Collect ViewModel state/effect flows here. */
    protected open fun setupObservers() = Unit

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

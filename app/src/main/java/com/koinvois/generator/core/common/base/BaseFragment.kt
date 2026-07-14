package com.koinvois.generator.core.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * ViewBinding scaffolding shared by every Fragment. Fragments only wire up
 * views/observers here — no business logic (that belongs in a UseCase
 * invoked from the ViewModel).
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = requireNotNull(_binding) { "Binding accessed outside onCreateView()/onDestroyView() window" }

    protected abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
    }

    /** Wire click listeners, adapters, navigation — no data loading. */
    protected open fun setupView() = Unit

    /** Collect ViewModel state/effect flows here. */
    protected open fun setupObservers() = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

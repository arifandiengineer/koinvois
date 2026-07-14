package com.koinvois.generator.core.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Every screen ViewModel extends this: one [StateFlow] for the immutable
 * screen state, a buffered [Channel] for one-shot effects, and a single
 * [onEvent] entry point for user actions. No business logic lives here —
 * subclasses delegate to UseCases.
 */
abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialState: State
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _uiEffect = Channel<Effect>(Channel.BUFFERED)
    val uiEffect: Flow<Effect> = _uiEffect.receiveAsFlow()

    protected val currentState: State
        get() = _uiState.value

    abstract fun onEvent(event: Event)

    protected fun setState(reduce: State.() -> State) {
        _uiState.value = currentState.reduce()
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch { _uiEffect.send(effect) }
    }
}

package com.mertcaliskanyurek.cmpbootstrap.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class ScreenModelBase<State, Event>(
    initialState: State
) : ScreenModel {

    internal val navigationEvent: SharedFlow<NavigationEvent>
        field = MutableSharedFlow<NavigationEvent>()

    val uiState: StateFlow<State>
        field = MutableStateFlow(initialState)

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect

    /**
     * Coroutine scope tied to this screen model's lifecycle.
     * Wraps Voyager's screenModelScope for testing purposes. Customers can override scope
     */
    protected val scope: CoroutineScope get() = screenModelScope

    protected fun emitNavigationEvent(event: NavigationEvent) {
        scope.launch { navigationEvent.emit(event) }
    }

    protected fun emitUiEffect(effect: UiEffect) {
        scope.launch { _uiEffect.emit(effect) }
    }

    /**
     * Convenience for launching coroutines within this screen model's scope.
     * Includes a default error handler that logs the error and optionally
     * provides a UI feedback.
     */
    protected fun safeLaunch(
        coroutineContext: CoroutineContext = EmptyCoroutineContext,
        onError: ((Throwable) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        scope.launch(coroutineContext + CoroutineExceptionHandler { _, throwable ->
            if (onError != null) {
                onError(throwable)
            } else {
                // Default error handling: emit a global error effect
                val message = throwable.message ?: "An unexpected error occurred"
                emitUiEffect(UiEffect.ShowSnackbar(message))
            }
        }) {
            block()
        }
    }

    /**
     * Convenience for updating the UI state.
     */
    protected fun updateState(reducer: (State) -> State) {
        uiState.update(reducer)
    }

    /**
     * Override to handle UI events. This is a suspend function, so you can
     * call suspending operations (e.g. emitNavigationEvent, use cases) directly
     * without needing to wrap in launch { }.
     */
    abstract fun handleUIEvent(event: Event)

}
package com.mertcaliskanyurek.cmpbootstrap.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class ScreenModelBase<State, Event>(
    initialState: State
) : ScreenModel {

    internal val navigationEvent: SharedFlow<NavigationEvent>
        field = MutableSharedFlow<NavigationEvent>()

    val state: StateFlow<State>
        field = MutableStateFlow(initialState)

    /**
     * Coroutine scope tied to this screen model's lifecycle.
     * Wraps Voyager's screenModelScope so consumers don't need the Voyager import.
     */
    protected val scope: CoroutineScope get() = screenModelScope

    /**
     * Override to handle UI events. This is a suspend function, so you can
     * call suspending operations (e.g. emitNavigationEvent, use cases) directly
     * without needing to wrap in launch { }.
     */
    protected abstract suspend fun handleUIEvent(event: Event)

    protected fun updateState(reducer: (State) -> State) {
        state.update(reducer)
    }

    protected suspend fun emitNavigationEvent(event: NavigationEvent) {
        navigationEvent.emit(event)
    }

    /**
     * Called from the UI to dispatch events. Automatically launches a coroutine
     * so handleUIEvent can be a suspend function.
     */
    fun emitUIEvent(event: Event) {
        scope.launch { handleUIEvent(event) }
    }

    /**
     * Convenience for launching coroutines within this screen model's scope.
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        scope.launch(block = block)
    }
}
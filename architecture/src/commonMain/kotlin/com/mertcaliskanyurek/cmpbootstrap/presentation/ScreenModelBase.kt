package com.mertcaliskanyurek.cmpbootstrap.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class ScreenModelBase<State, Event>(
    initialState: State
) : ScreenModel {

    internal val navigationEvent: SharedFlow<NavigationEvent>
        field = MutableSharedFlow<NavigationEvent>()

    val state: StateFlow<State>
        field = MutableStateFlow(initialState)

    protected abstract fun handleUIEvent(event: Event)

    protected fun updateState(reducer: (State) -> State) {
        state.update(reducer)
    }

    protected suspend fun emitNavigationEvent(event: NavigationEvent) {
        navigationEvent.emit(event)
    }

    fun emitUIEvent(event: Event) {
        handleUIEvent(event)
    }
}
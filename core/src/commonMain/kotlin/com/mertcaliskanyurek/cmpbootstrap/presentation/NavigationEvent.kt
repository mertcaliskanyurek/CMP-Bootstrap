package com.mertcaliskanyurek.cmpbootstrap.presentation

import cafe.adriel.voyager.core.screen.Screen

sealed class NavigationEvent {
    data object NavigateBack : NavigationEvent()
    data class Push(val route: Route) : NavigationEvent()
    data class Replace(val route: Route) : NavigationEvent()
    data class ReplaceAll(val route: Route) : NavigationEvent()
    data class PopUntil(val predicate: (Screen) -> Boolean) : NavigationEvent()
}
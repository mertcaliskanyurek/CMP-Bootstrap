package com.mertcaliskanyurek.cmpbootstrap.presentation

sealed class NavigationEvent {
    data object NavigateBack :NavigationEvent()
    data class Push(val route: Route) : NavigationEvent()
    data class Replace(val route: Route) : NavigationEvent()
}
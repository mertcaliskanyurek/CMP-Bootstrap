package com.mertcaliskanyurek.cmpbootstrap.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import androidx.lifecycle.compose.collectAsStateWithLifecycle

interface ScreenBase<State, Event, SM : ScreenModelBase<State, Event>> : Screen {

    @Composable
    override fun Content() {
        val viewModel: SM = provideScreenModel()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(viewModel) {
            viewModel.navigationEvent.collect { event ->
                handleNavigationEvent(navigator, event)
            }
        }

        ScreenContent(
            state = viewModel.state.collectAsStateWithLifecycle().value,
            emitUIEvent = viewModel::emitUIEvent
        )
    }

    @Composable
    fun ScreenContent(
        state: State,
        emitUIEvent: (Event) -> Unit
    )

    @Composable
    fun provideScreenModel(): SM

    private fun handleNavigationEvent(
        navigator: Navigator,
        event: NavigationEvent
    ) {
        when (event) {
            is NavigationEvent.NavigateBack -> {
                navigator.pop()
            }

            is NavigationEvent.Push -> {
                navigator.push(event.route.getScreen())
            }

            is NavigationEvent.Replace -> {
                navigator.replace(event.route.getScreen())
            }
        }
    }
}
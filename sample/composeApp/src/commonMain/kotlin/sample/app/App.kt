package sample.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.koin.compose.KoinApplication
import sample.app.di.appModule
import sample.app.presentation.postlist.PostListScreen

@Composable
fun App() {
    KoinApplication(application = { modules(appModule) }) {
        Navigator(screen = PostListScreen())
    }
}

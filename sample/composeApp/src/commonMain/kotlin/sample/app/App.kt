package sample.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.mertcaliskanyurek.bootstrap.datastore.DataStoreContext
import org.koin.compose.KoinApplication
import sample.app.di.appModule
import sample.app.presentation.postlist.PostListScreen

@Composable
fun App(dataStoreContext: DataStoreContext) {
    KoinApplication(application = { modules(appModule(dataStoreContext)) }) {
        Navigator(screen = PostListScreen())
    }
}

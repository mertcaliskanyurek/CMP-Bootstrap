import androidx.compose.ui.window.ComposeUIViewController
import com.mertcaliskanyurek.bootstrap.datastore.DataStoreContext
import platform.UIKit.UIViewController
import sample.app.App

fun MainViewController(): UIViewController = ComposeUIViewController { App(DataStoreContext()) }

package sample.app.presentation.savedposts

import com.mertcaliskanyurek.cmpbootstrap.presentation.Route
import com.mertcaliskanyurek.cmpbootstrap.presentation.ScreenBase

class SavedPostsRoute : Route {
    override fun getScreen(): ScreenBase<*, *, *> = SavedPostsScreen()
}

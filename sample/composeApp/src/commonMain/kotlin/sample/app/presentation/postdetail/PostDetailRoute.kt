package sample.app.presentation.postdetail

import com.mertcaliskanyurek.cmpbootstrap.presentation.Route
import com.mertcaliskanyurek.cmpbootstrap.presentation.ScreenBase

data class PostDetailRoute(val postId: Int) : Route {
    override fun getScreen(): ScreenBase<*, *, *> = PostDetailScreen(postId)
}

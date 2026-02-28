package sample.app.presentation.savedposts

import cafe.adriel.voyager.core.model.screenModelScope
import com.mertcaliskanyurek.cmpbootstrap.presentation.NavigationEvent
import com.mertcaliskanyurek.cmpbootstrap.presentation.ScreenModelBase
import kotlinx.coroutines.launch
import sample.app.data.model.Post
import sample.app.domain.ObserveSavedPostsUseCase
import sample.app.domain.RemovePostUseCase
import sample.app.presentation.postdetail.PostDetailRoute

data class SavedPostsState(
    val posts: List<Post> = emptyList()
)

sealed class SavedPostsEvent {
    data class OnPostClick(val postId: Int) : SavedPostsEvent()
    data class RemovePost(val postId: Int) : SavedPostsEvent()
    data object NavigateBack : SavedPostsEvent()
}

class SavedPostsScreenModel(
    private val observeSavedPostsUseCase: ObserveSavedPostsUseCase,
    private val removePostUseCase: RemovePostUseCase
) : ScreenModelBase<SavedPostsState, SavedPostsEvent>(SavedPostsState()) {

    init {
        screenModelScope.launch {
            observeSavedPostsUseCase().collect { posts ->
                updateState { it.copy(posts = posts) }
            }
        }
    }

    override fun handleUIEvent(event: SavedPostsEvent) {
        when (event) {
            is SavedPostsEvent.OnPostClick -> screenModelScope.launch {
                emitNavigationEvent(NavigationEvent.Push(PostDetailRoute(event.postId)))
            }
            is SavedPostsEvent.RemovePost -> screenModelScope.launch {
                removePostUseCase(event.postId)
            }
            SavedPostsEvent.NavigateBack -> screenModelScope.launch {
                emitNavigationEvent(NavigationEvent.NavigateBack)
            }
        }
    }
}

package sample.app.presentation.postlist

import com.mertcaliskanyurek.cmpbootstrap.presentation.NavigationEvent
import com.mertcaliskanyurek.cmpbootstrap.presentation.ScreenModelBase
import sample.app.data.model.Post
import sample.app.domain.GetPostsUseCase
import sample.app.presentation.postdetail.PostDetailRoute
import sample.app.presentation.savedposts.SavedPostsRoute

data class PostListState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class PostListEvent {
    data object LoadPosts : PostListEvent()
    data class OnPostClick(val postId: Int) : PostListEvent()
    data object NavigateToSavedPosts : PostListEvent()
}

class PostListScreenModel(
    private val getPostsUseCase: GetPostsUseCase
) : ScreenModelBase<PostListState, PostListEvent>(PostListState()) {

    init {
        loadPosts()
    }

    override suspend fun handleUIEvent(event: PostListEvent) {
        when (event) {
            PostListEvent.LoadPosts -> loadPosts()
            is PostListEvent.OnPostClick -> {
                emitNavigationEvent(NavigationEvent.Push(PostDetailRoute(event.postId)))
            }
            PostListEvent.NavigateToSavedPosts -> {
                emitNavigationEvent(NavigationEvent.Push(SavedPostsRoute()))
            }
        }
    }

    private fun loadPosts() {
        launch {
            updateState { it.copy(isLoading = true, error = null) }
            val result = getPostsUseCase(Unit)
            result.fold(
                onSuccess = { posts ->
                    updateState { it.copy(posts = posts, isLoading = false) }
                },
                onFailure = { error ->
                    updateState { it.copy(error = error.message ?: "Unknown error", isLoading = false) }
                }
            )
        }
    }
}

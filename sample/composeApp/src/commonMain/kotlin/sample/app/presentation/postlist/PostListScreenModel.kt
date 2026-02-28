package sample.app.presentation.postlist

import cafe.adriel.voyager.core.model.screenModelScope
import com.mertcaliskanyurek.cmpbootstrap.presentation.NavigationEvent
import com.mertcaliskanyurek.cmpbootstrap.presentation.ScreenModelBase
import kotlinx.coroutines.launch
import sample.app.data.model.Post
import sample.app.domain.GetPostsUseCase
import sample.app.presentation.postdetail.PostDetailRoute

data class PostListState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class PostListEvent {
    data object LoadPosts : PostListEvent()
    data class OnPostClick(val postId: Int) : PostListEvent()
}

class PostListScreenModel(
    private val getPostsUseCase: GetPostsUseCase
) : ScreenModelBase<PostListState, PostListEvent>(PostListState()) {

    init {
        loadPosts()
    }

    override fun handleUIEvent(event: PostListEvent) {
        when (event) {
            PostListEvent.LoadPosts -> loadPosts()
            is PostListEvent.OnPostClick -> screenModelScope.launch {
                emitNavigationEvent(
                    NavigationEvent.Push(PostDetailRoute(event.postId))
                )
            }
        }
    }

    private fun loadPosts() {
        screenModelScope.launch {
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

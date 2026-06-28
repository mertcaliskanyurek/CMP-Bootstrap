package sample.app.presentation.postlist

import com.mertcaliskanyurek.cmpbootstrap.domain.AppError
import com.mertcaliskanyurek.cmpbootstrap.presentation.LoadableState
import com.mertcaliskanyurek.cmpbootstrap.presentation.NavigationEvent
import com.mertcaliskanyurek.cmpbootstrap.presentation.ScreenModelBase
import com.mertcaliskanyurek.cmpbootstrap.presentation.UiError
import com.mertcaliskanyurek.cmpbootstrap.presentation.toUiError
import sample.app.data.model.LocalPost
import sample.app.data.model.Post
import sample.app.domain.GetPostsUseCase
import sample.app.domain.ObserveLocalPostsUseCase
import sample.app.presentation.postdetail.PostDetailRoute
import sample.app.presentation.savedposts.SavedPostsRoute
import sample.app.presentation.writepost.WritePostRoute


data class PostListUiState(
    val posts: List<Post> = emptyList(),
    val localPosts: List<LocalPost> = emptyList(),
    override val isLoading: Boolean = false,
    override val error: UiError? = null
) : LoadableState

sealed class PostListEvent {
    data object LoadPosts : PostListEvent()
    data class OnPostClick(val postId: Int) : PostListEvent()
    data object NavigateToSavedPosts : PostListEvent()
    data object NavigateToWritePost : PostListEvent()
}

class PostListScreenModel(
    private val getPostsUseCase: GetPostsUseCase,
    private val observeLocalPostsUseCase: ObserveLocalPostsUseCase
) : ScreenModelBase<PostListUiState, PostListEvent>(PostListUiState()) {

    init {
        loadPosts()
        observeLocalPosts()
    }

    override fun handleUIEvent(event: PostListEvent) {
        when (event) {
            PostListEvent.LoadPosts -> loadPosts()
            is PostListEvent.OnPostClick -> {
                emitNavigationEvent(NavigationEvent.Push(PostDetailRoute(event.postId)))
            }
            PostListEvent.NavigateToSavedPosts -> {
                emitNavigationEvent(NavigationEvent.Push(SavedPostsRoute()))
            }
            PostListEvent.NavigateToWritePost -> {
                emitNavigationEvent(NavigationEvent.Push(WritePostRoute()))
            }
        }
    }

    private fun observeLocalPosts() {
        safeLaunch {
            observeLocalPostsUseCase().collect { result ->
                result.onSuccess { localPosts ->
                    updateState { it.copy(localPosts = localPosts) }
                }
            }
        }
    }

    private fun loadPosts() {
        safeLaunch {
            updateState { it.copy(isLoading = true, error = null) }
            val result = getPostsUseCase()
            result.fold(
                onSuccess = { posts ->
                    updateState { it.copy(posts = posts, isLoading = false) }
                },
                onFailure = { throwable ->
                    val appError = throwable as? AppError ?: AppError.Unexpected(throwable)
                    updateState {
                        it.copy(
                            error = appError.toUiError(onRetry = { loadPosts() }),
                            isLoading = false
                        )
                    }
                }
            )
        }
    }
}

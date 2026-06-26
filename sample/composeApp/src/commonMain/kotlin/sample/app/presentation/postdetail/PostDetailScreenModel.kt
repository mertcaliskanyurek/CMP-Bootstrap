package sample.app.presentation.postdetail

import com.mertcaliskanyurek.cmpbootstrap.presentation.NavigationEvent
import com.mertcaliskanyurek.cmpbootstrap.presentation.ScreenModelBase
import kotlinx.coroutines.async
import sample.app.data.model.Comment
import sample.app.data.model.Post
import sample.app.domain.GetPostCommentsUseCase
import sample.app.domain.GetPostUseCase
import sample.app.domain.ObserveSavedPostsUseCase
import sample.app.domain.RemovePostUseCase
import sample.app.domain.SavePostUseCase

data class PostDetailState(
    val post: Post? = null,
    val comments: List<Comment> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)

sealed class PostDetailEvent {
    data object NavigateBack : PostDetailEvent()
    data object Retry : PostDetailEvent()
    data object SavePost : PostDetailEvent()
    data object RemovePost : PostDetailEvent()
}

class PostDetailScreenModel(
    private val postId: Int,
    private val getPostUseCase: GetPostUseCase,
    private val getPostCommentsUseCase: GetPostCommentsUseCase,
    private val observeSavedPostsUseCase: ObserveSavedPostsUseCase,
    private val savePostUseCase: SavePostUseCase,
    private val removePostUseCase: RemovePostUseCase
) : ScreenModelBase<PostDetailState, PostDetailEvent>(PostDetailState()) {

    init {
        loadPostAndComments()
        observeSavedState()
    }

    private fun observeSavedState() {
        safeLaunch {
            observeSavedPostsUseCase().collect { result ->
                result.onSuccess { savedPosts ->
                    updateState { it.copy(isSaved = savedPosts.any { post -> post.id == postId }) }
                }
            }
        }
    }

    override fun handleUIEvent(event: PostDetailEvent) {
        when (event) {
            PostDetailEvent.NavigateBack -> emitNavigationEvent(NavigationEvent.NavigateBack)
            PostDetailEvent.Retry -> loadPostAndComments()
            PostDetailEvent.SavePost -> savePost()
            PostDetailEvent.RemovePost -> removePost()
        }
    }

    private fun savePost() {
        safeLaunch {
            uiState.value.post?.let { savePostUseCase(it) }
        }
    }

    private fun removePost() {
        safeLaunch {
            removePostUseCase(postId)
        }
    }

    private fun loadPostAndComments() {
        safeLaunch {
            updateState { it.copy(isLoading = true, error = null) }
            val postDeferred = async { getPostUseCase(postId) }
            val commentsDeferred = async { getPostCommentsUseCase(postId) }
            val postResult = postDeferred.await()
            val commentsResult = commentsDeferred.await()
            if (postResult.isSuccess && commentsResult.isSuccess) {
                updateState {
                    it.copy(
                        post = postResult.getOrNull(),
                        comments = commentsResult.getOrNull() ?: emptyList(),
                        isLoading = false
                    )
                }
            } else {
                val error = (postResult.exceptionOrNull() ?: commentsResult.exceptionOrNull())
                    ?.message ?: "Unknown error"
                updateState { it.copy(error = error, isLoading = false) }
            }
        }
    }
}

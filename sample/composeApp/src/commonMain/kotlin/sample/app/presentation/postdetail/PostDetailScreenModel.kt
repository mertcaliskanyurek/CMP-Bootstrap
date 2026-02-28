package sample.app.presentation.postdetail

import cafe.adriel.voyager.core.model.screenModelScope
import com.mertcaliskanyurek.cmpbootstrap.presentation.NavigationEvent
import com.mertcaliskanyurek.cmpbootstrap.presentation.ScreenModelBase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import sample.app.data.model.Comment
import sample.app.data.model.Post
import sample.app.domain.GetPostCommentsUseCase
import sample.app.domain.GetPostUseCase

data class PostDetailState(
    val post: Post? = null,
    val comments: List<Comment> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class PostDetailEvent {
    data object NavigateBack : PostDetailEvent()
    data object Retry : PostDetailEvent()
}

class PostDetailScreenModel(
    private val postId: Int,
    private val getPostUseCase: GetPostUseCase,
    private val getPostCommentsUseCase: GetPostCommentsUseCase
) : ScreenModelBase<PostDetailState, PostDetailEvent>(PostDetailState()) {

    init {
        loadPostAndComments()
    }

    override fun handleUIEvent(event: PostDetailEvent) {
        when (event) {
            PostDetailEvent.NavigateBack -> screenModelScope.launch {
                emitNavigationEvent(NavigationEvent.NavigateBack)
            }
            PostDetailEvent.Retry -> loadPostAndComments()
        }
    }

    private fun loadPostAndComments() {
        screenModelScope.launch {
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

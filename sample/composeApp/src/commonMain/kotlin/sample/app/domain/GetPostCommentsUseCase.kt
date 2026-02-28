package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.UseCase
import sample.app.data.model.Comment
import sample.app.data.repository.PostRepository

class GetPostCommentsUseCase(private val repository: PostRepository) : UseCase<Int, List<Comment>>() {
    override suspend fun execute(params: Int): List<Comment> =
        repository.getPostComments(params)
}

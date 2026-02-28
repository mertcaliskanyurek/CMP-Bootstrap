package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.NoParamUseCase
import sample.app.data.model.Post
import sample.app.data.repository.PostRepository

class GetPostsUseCase(private val repository: PostRepository) : NoParamUseCase<List<Post>>() {
    override suspend fun execute(params: Unit): Result<List<Post>> =
        Result.success(repository.getPosts())
}

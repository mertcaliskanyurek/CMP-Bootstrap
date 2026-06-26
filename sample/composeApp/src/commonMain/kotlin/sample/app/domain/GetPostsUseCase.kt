package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.NoParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import sample.app.data.model.Post
import sample.app.data.repository.PostRepository

class GetPostsUseCase(
    private val repository: PostRepository
) : NoParamUseCase<List<Post>>(Dispatchers.IO) {
    override suspend fun execute(params: Unit): List<Post> = repository.getPosts()
}

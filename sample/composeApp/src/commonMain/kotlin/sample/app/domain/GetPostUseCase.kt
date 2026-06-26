package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import sample.app.data.model.Post
import sample.app.data.repository.PostRepository

class GetPostUseCase(
    private val repository: PostRepository
) : UseCase<Int, Post>(Dispatchers.IO) {
    override suspend fun execute(params: Int): Post = repository.getPost(params)
}

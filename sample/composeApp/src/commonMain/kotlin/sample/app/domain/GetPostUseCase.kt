package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.UseCase
import sample.app.data.model.Post
import sample.app.data.repository.PostRepository

class GetPostUseCase(private val repository: PostRepository) : UseCase<Int, Post>() {
    override suspend fun execute(params: Int): Post = repository.getPost(params)
}

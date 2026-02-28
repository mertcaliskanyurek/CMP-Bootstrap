package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.UseCase
import sample.app.data.model.Post
import sample.app.data.repository.SavedPostsRepository

class SavePostUseCase(private val repository: SavedPostsRepository) : UseCase<Post, Unit>() {
    override suspend fun execute(params: Post) = repository.savePost(params)
}

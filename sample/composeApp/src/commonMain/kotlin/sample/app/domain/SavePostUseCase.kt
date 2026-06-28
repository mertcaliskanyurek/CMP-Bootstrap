package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.NoResultUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import sample.app.data.model.Post
import sample.app.data.repository.SavedPostsRepository

class SavePostUseCase(
    private val repository: SavedPostsRepository
) : NoResultUseCase<Post>(Dispatchers.IO) {
    override suspend fun execute(params: Post) = repository.savePost(params)
}

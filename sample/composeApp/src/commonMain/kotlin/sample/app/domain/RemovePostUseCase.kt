package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import sample.app.data.repository.SavedPostsRepository

class RemovePostUseCase(
    private val repository: SavedPostsRepository
) : UseCase<Int, Unit>(Dispatchers.IO) {
    override suspend fun execute(params: Int) = repository.removePost(params)
}

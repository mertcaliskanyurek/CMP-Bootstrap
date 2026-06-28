package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.NoResultUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import sample.app.data.repository.SavedPostsRepository

class RemovePostUseCase(
    private val repository: SavedPostsRepository
) : NoResultUseCase<Int>(Dispatchers.IO) {
    override suspend fun execute(params: Int) = repository.removePost(params)
}

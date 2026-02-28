package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.UseCase
import sample.app.data.repository.SavedPostsRepository

class RemovePostUseCase(private val repository: SavedPostsRepository) : UseCase<Int, Unit>() {
    override suspend fun execute(params: Int): Result<Unit> {
        repository.removePost(params)
        return Result.success(Unit)
    }
}

package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import sample.app.data.model.LocalPost
import sample.app.data.repository.LocalPostsRepository

class SaveLocalPostUseCase(
    private val repository: LocalPostsRepository
) : UseCase<LocalPost, Unit>(Dispatchers.IO) {
    override suspend fun execute(params: LocalPost) = repository.saveLocalPost(params)
}

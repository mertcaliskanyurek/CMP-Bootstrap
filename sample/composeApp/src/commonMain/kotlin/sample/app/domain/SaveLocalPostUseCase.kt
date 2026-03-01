package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.UseCase
import sample.app.data.model.LocalPost
import sample.app.data.repository.LocalPostsRepository

class SaveLocalPostUseCase(
    private val repository: LocalPostsRepository
) : UseCase<LocalPost, Unit>() {
    override suspend fun execute(params: LocalPost) = repository.saveLocalPost(params)
}

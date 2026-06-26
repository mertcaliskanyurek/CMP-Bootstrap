package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.NoParamFlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import sample.app.data.model.LocalPost
import sample.app.data.repository.LocalPostsRepository

class ObserveLocalPostsUseCase(
    private val repository: LocalPostsRepository
) : NoParamFlowUseCase<List<LocalPost>>(Dispatchers.IO) {
    override fun execute(params: Unit): Flow<List<LocalPost>> =
        repository.observeLocalPosts()
}

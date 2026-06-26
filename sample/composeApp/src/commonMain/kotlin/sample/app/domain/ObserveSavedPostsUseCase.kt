package sample.app.domain

import com.mertcaliskanyurek.cmpbootstrap.domain.NoParamFlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import sample.app.data.model.Post
import sample.app.data.repository.SavedPostsRepository

class ObserveSavedPostsUseCase(
    private val repository: SavedPostsRepository
) : NoParamFlowUseCase<List<Post>>(Dispatchers.IO) {

    override fun execute(params: Unit): Flow<List<Post>> =
        repository.observeSavedPosts()
}

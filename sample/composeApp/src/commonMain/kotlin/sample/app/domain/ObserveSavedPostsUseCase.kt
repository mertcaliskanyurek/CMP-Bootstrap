package sample.app.domain

import kotlinx.coroutines.flow.Flow
import sample.app.data.model.Post
import sample.app.data.repository.SavedPostsRepository

class ObserveSavedPostsUseCase(private val repository: SavedPostsRepository) {
    operator fun invoke(): Flow<List<Post>> = repository.observeSavedPosts()
}

package sample.app.data.repository

import com.mertcaliskanyurek.bootstrap.datastore.KeyValueStorage
import com.mertcaliskanyurek.bootstrap.datastore.StorageKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import sample.app.data.model.Post

class SavedPostsRepository(private val storage: KeyValueStorage) {

    private val key = StorageKey.StringKey("saved_posts")

    fun observeSavedPosts(): Flow<List<Post>> = storage.observe(key).map { json ->
        if (json != null) Json.decodeFromString(json) else emptyList()
    }

    suspend fun savePost(post: Post) {
        val current = getSavedPosts().toMutableList()
        if (current.none { it.id == post.id }) {
            current.add(post)
            storage.put(key, Json.encodeToString(current))
        }
    }

    suspend fun removePost(postId: Int) {
        val current = getSavedPosts().toMutableList()
        current.removeAll { it.id == postId }
        storage.put(key, Json.encodeToString(current))
    }

    private suspend fun getSavedPosts(): List<Post> {
        val json = storage.get(key) ?: return emptyList()
        return Json.decodeFromString(json)
    }
}

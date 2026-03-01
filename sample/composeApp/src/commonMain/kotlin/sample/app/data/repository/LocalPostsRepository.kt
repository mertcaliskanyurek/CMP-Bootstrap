package sample.app.data.repository

import com.mertcaliskanyurek.bootstrap.datastore.KeyValueStorage
import com.mertcaliskanyurek.bootstrap.datastore.StorageKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import sample.app.data.model.LocalPost

class LocalPostsRepository(private val storage: KeyValueStorage) {

    private val key = StorageKey.StringKey("local_posts")

    fun observeLocalPosts(): Flow<List<LocalPost>> = storage.observe(key).map { json ->
        if (json != null) Json.decodeFromString(json) else emptyList()
    }

    suspend fun saveLocalPost(post: LocalPost) {
        val current = getLocalPosts().toMutableList()
        current.add(post)
        storage.put(key, Json.encodeToString(current))
    }

    private suspend fun getLocalPosts(): List<LocalPost> {
        val json = storage.get(key) ?: return emptyList()
        return Json.decodeFromString(json)
    }
}

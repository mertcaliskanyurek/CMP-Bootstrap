package sample.app.data.repository

import com.mertcaliskanyurek.bootstrap.networking.ApiClient
import sample.app.data.model.Comment
import sample.app.data.model.Post

class PostRepository(private val apiClient: ApiClient) {
    suspend fun getPosts(): List<Post> = apiClient.get("posts")
    suspend fun getPost(id: Int): Post = apiClient.get("posts/$id")
    suspend fun getPostComments(postId: Int): List<Comment> = apiClient.get("posts/$postId/comments")
}

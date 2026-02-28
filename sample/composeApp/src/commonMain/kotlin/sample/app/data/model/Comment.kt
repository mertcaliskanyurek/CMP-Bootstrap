package sample.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Int,
    val postId: Int,
    val name: String,
    val email: String,
    val body: String
)

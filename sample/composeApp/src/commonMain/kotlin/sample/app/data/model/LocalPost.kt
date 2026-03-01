package sample.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LocalPost(
    val id: String,
    val title: String,
    val body: String,
    val attachedFileUri: String? = null,
    val attachedFileName: String? = null
)

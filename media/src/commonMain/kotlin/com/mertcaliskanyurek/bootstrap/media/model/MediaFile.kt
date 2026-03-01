package com.mertcaliskanyurek.bootstrap.media.model

data class MediaFile(
    val uri: String,
    val name: String,
    val mimeType: String?,
    val size: Long?,
    val mediaType: MediaType
)

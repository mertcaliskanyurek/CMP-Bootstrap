package com.mertcaliskanyurek.bootstrap.media.model

data class MediaPickerConfig(
    val allowedTypes: Set<MediaType> = setOf(MediaType.IMAGE),
    val allowMultiple: Boolean = false,
    val maxSelectionCount: Int = 0,
    val mimeTypeFilter: List<String> = emptyList()
)

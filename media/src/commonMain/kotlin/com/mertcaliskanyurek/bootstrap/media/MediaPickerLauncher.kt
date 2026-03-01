package com.mertcaliskanyurek.bootstrap.media

import com.mertcaliskanyurek.bootstrap.media.model.MediaPickerConfig
import com.mertcaliskanyurek.bootstrap.media.model.MediaType

object MediaPickerLauncher {

    fun imagePicker(allowMultiple: Boolean = false): MediaPickerConfig =
        MediaPickerConfig(
            allowedTypes = setOf(MediaType.IMAGE),
            allowMultiple = allowMultiple
        )

    fun videoPicker(allowMultiple: Boolean = false): MediaPickerConfig =
        MediaPickerConfig(
            allowedTypes = setOf(MediaType.VIDEO),
            allowMultiple = allowMultiple
        )

    fun visualMediaPicker(
        allowMultiple: Boolean = false,
        maxCount: Int = 0
    ): MediaPickerConfig =
        MediaPickerConfig(
            allowedTypes = setOf(MediaType.IMAGE, MediaType.VIDEO),
            allowMultiple = allowMultiple,
            maxSelectionCount = maxCount
        )

    fun filePicker(
        mimeTypes: List<String> = emptyList(),
        allowMultiple: Boolean = false
    ): MediaPickerConfig =
        MediaPickerConfig(
            allowedTypes = setOf(MediaType.FILE),
            allowMultiple = allowMultiple,
            mimeTypeFilter = mimeTypes
        )
}

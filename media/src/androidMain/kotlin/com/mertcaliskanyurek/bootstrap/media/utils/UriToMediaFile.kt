package com.mertcaliskanyurek.bootstrap.media.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.mertcaliskanyurek.bootstrap.media.model.MediaFile
import com.mertcaliskanyurek.bootstrap.media.model.MediaType

internal fun Uri.toMediaFile(contentResolver: ContentResolver, fallbackMediaType: MediaType): MediaFile {
    var name = ""
    var size: Long? = null

    contentResolver.query(this, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        if (cursor.moveToFirst()) {
            if (nameIndex >= 0) name = cursor.getString(nameIndex) ?: ""
            if (sizeIndex >= 0 && !cursor.isNull(sizeIndex)) size = cursor.getLong(sizeIndex)
        }
    }

    val mimeType = contentResolver.getType(this)
    val mediaType = when {
        mimeType?.startsWith("image/") == true -> MediaType.IMAGE
        mimeType?.startsWith("video/") == true -> MediaType.VIDEO
        else -> fallbackMediaType
    }

    return MediaFile(
        uri = toString(),
        name = name,
        mimeType = mimeType,
        size = size,
        mediaType = mediaType
    )
}

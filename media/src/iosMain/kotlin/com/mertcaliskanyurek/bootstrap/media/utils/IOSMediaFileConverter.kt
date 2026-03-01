package com.mertcaliskanyurek.bootstrap.media.utils

import com.mertcaliskanyurek.bootstrap.media.model.MediaFile
import com.mertcaliskanyurek.bootstrap.media.model.MediaType
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileSize
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.UniformTypeIdentifiers.UTType

@OptIn(ExperimentalForeignApi::class)
internal fun NSURL.toMediaFile(mediaType: MediaType): MediaFile {
    val name = lastPathComponent ?: ""
    val path = path ?: ""

    val size: Long? = runCatching {
        val attrs = NSFileManager.defaultManager.attributesOfItemAtPath(path, error = null)
        (attrs?.get(NSFileSize) as? NSNumber)?.longValue
    }.getOrNull()

    val extension = pathExtension ?: ""
    val mimeType = if (extension.isNotEmpty()) {
        UTType.typeWithFilenameExtension(extension)?.preferredMIMEType
    } else {
        null
    }

    val resolvedMediaType = when {
        mimeType?.startsWith("image/") == true -> MediaType.IMAGE
        mimeType?.startsWith("video/") == true -> MediaType.VIDEO
        else -> mediaType
    }

    return MediaFile(
        uri = absoluteString ?: path,
        name = name,
        mimeType = mimeType,
        size = size,
        mediaType = resolvedMediaType
    )
}

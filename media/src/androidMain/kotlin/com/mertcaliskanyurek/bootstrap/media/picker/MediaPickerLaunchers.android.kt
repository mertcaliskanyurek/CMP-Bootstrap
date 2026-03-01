package com.mertcaliskanyurek.bootstrap.media.picker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mertcaliskanyurek.bootstrap.media.camera.AndroidCameraImpl
import com.mertcaliskanyurek.bootstrap.media.model.MediaFile
import com.mertcaliskanyurek.bootstrap.media.model.MediaPickerConfig
import com.mertcaliskanyurek.bootstrap.media.model.MediaType
import com.mertcaliskanyurek.bootstrap.media.utils.toMediaFile

@Composable
actual fun rememberMediaPickerLauncher(
    context: MediaPickerContext,
    config: MediaPickerConfig,
    onResult: (List<MediaFile>) -> Unit
): IMediaPickerLauncher {
    val androidContext = context.context
    val contentResolver = androidContext.contentResolver

    val isFileType = MediaType.FILE in config.allowedTypes
    val hasImage = MediaType.IMAGE in config.allowedTypes
    val hasVideo = MediaType.VIDEO in config.allowedTypes

    val mimeTypes: Array<String> = when {
        isFileType -> if (config.mimeTypeFilter.isNotEmpty()) {
            config.mimeTypeFilter.toTypedArray()
        } else {
            arrayOf("*/*")
        }
        hasImage && hasVideo -> arrayOf("image/*", "video/*")
        hasImage -> arrayOf("image/*")
        hasVideo -> arrayOf("video/*")
        else -> arrayOf("*/*")
    }

    val visualMediaType: androidx.activity.result.PickVisualMediaRequest.() -> Unit = {}

    // Single document picker
    val singleDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        val result = uri?.let { listOf(it.toMediaFile(contentResolver, MediaType.FILE)) } ?: emptyList()
        onResult(result)
    }

    // Multiple document picker
    val multipleDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        onResult(uris.map { it.toMediaFile(contentResolver, MediaType.FILE) })
    }

    // Single visual media picker
    val singleVisualMediaType = when {
        hasImage && hasVideo -> ActivityResultContracts.PickVisualMedia.ImageAndVideo
        hasVideo -> ActivityResultContracts.PickVisualMedia.VideoOnly
        else -> ActivityResultContracts.PickVisualMedia.ImageOnly
    }

    val singleVisualLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        val result = uri?.let {
            val mt = if (it.toString().contains("video")) MediaType.VIDEO else MediaType.IMAGE
            listOf(it.toMediaFile(contentResolver, mt))
        } ?: emptyList()
        onResult(result)
    }

    // Multiple visual media picker
    val maxItems = if (config.maxSelectionCount > 0) config.maxSelectionCount else Int.MAX_VALUE
    val multipleVisualLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = if (maxItems == Int.MAX_VALUE) 99 else maxItems
        )
    ) { uris ->
        onResult(uris.map { uri ->
            val mt = if (uri.toString().contains("video")) MediaType.VIDEO else MediaType.IMAGE
            uri.toMediaFile(contentResolver, mt)
        })
    }

    return remember(config) {
        object : IMediaPickerLauncher {
            override fun launch() {
                when {
                    isFileType && config.allowMultiple ->
                        multipleDocumentLauncher.launch(mimeTypes)
                    isFileType ->
                        singleDocumentLauncher.launch(mimeTypes)
                    config.allowMultiple ->
                        multipleVisualLauncher.launch(
                            androidx.activity.result.PickVisualMediaRequest(singleVisualMediaType)
                        )
                    else ->
                        singleVisualLauncher.launch(
                            androidx.activity.result.PickVisualMediaRequest(singleVisualMediaType)
                        )
                }
            }
        }
    }
}

@Composable
actual fun rememberCameraLauncher(
    context: MediaPickerContext,
    captureType: MediaType,
    onResult: (MediaFile?) -> Unit
): ICameraLauncher {
    val androidContext = context.context
    val contentResolver = androidContext.contentResolver

    var tempUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = tempUri
        if (success && uri != null) {
            onResult(uri.toMediaFile(contentResolver, MediaType.IMAGE))
        } else {
            onResult(null)
        }
        tempUri = null
    }

    val captureVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        val uri = tempUri
        if (success && uri != null) {
            onResult(uri.toMediaFile(contentResolver, MediaType.VIDEO))
        } else {
            onResult(null)
        }
        tempUri = null
    }

    return remember(captureType) {
        object : ICameraLauncher {
            override fun launch() {
                when (captureType) {
                    MediaType.IMAGE -> {
                        val uri = AndroidCameraImpl.createTempImageUri(androidContext)
                        tempUri = uri
                        takePictureLauncher.launch(uri)
                    }
                    MediaType.VIDEO -> {
                        val uri = AndroidCameraImpl.createTempVideoUri(androidContext)
                        tempUri = uri
                        captureVideoLauncher.launch(uri)
                    }
                    MediaType.FILE -> {
                        // FILE type not meaningful for camera; treat as image
                        val uri = AndroidCameraImpl.createTempImageUri(androidContext)
                        tempUri = uri
                        takePictureLauncher.launch(uri)
                    }
                }
            }
        }
    }
}

package com.mertcaliskanyurek.bootstrap.media.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mertcaliskanyurek.bootstrap.media.camera.UIImagePickerDelegate
import com.mertcaliskanyurek.bootstrap.media.model.MediaFile
import com.mertcaliskanyurek.bootstrap.media.model.MediaPickerConfig
import com.mertcaliskanyurek.bootstrap.media.model.MediaType
import kotlinx.cinterop.ExperimentalForeignApi
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UniformTypeIdentifiers.UTType
import platform.UniformTypeIdentifiers.UTTypeContent
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.UniformTypeIdentifiers.UTTypeMovie

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberMediaPickerLauncher(
    context: MediaPickerContext,
    config: MediaPickerConfig,
    onResult: (List<MediaFile>) -> Unit
): IMediaPickerLauncher {
    // Hold strong references to delegates to prevent ARC from collecting them
    val delegateRefs = remember { mutableListOf<Any>() }

    return remember(config) {
        object : IMediaPickerLauncher {
            override fun launch() {
                val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
                    ?: return

                delegateRefs.clear()

                val isFileType = MediaType.FILE in config.allowedTypes
                val hasImage = MediaType.IMAGE in config.allowedTypes
                val hasVideo = MediaType.VIDEO in config.allowedTypes

                if (isFileType) {
                    val utTypes: List<UTType> = if (config.mimeTypeFilter.isNotEmpty()) {
                        config.mimeTypeFilter.mapNotNull { mime ->
                            UTType.typeWithMIMEType(mime)
                        }.ifEmpty { listOf(UTTypeContent) }
                    } else {
                        listOf(UTTypeContent)
                    }

                    val delegate = UIDocumentPickerDelegate(onResult)
                    delegateRefs.add(delegate)

                    val picker = UIDocumentPickerViewController(forOpeningContentTypes = utTypes)
                    picker.allowsMultipleSelection = config.allowMultiple
                    picker.delegate = delegate
                    rootVC.presentViewController(picker, animated = true, completion = null)
                } else {
                    val phConfig = PHPickerConfiguration()
                    phConfig.selectionLimit = when {
                        !config.allowMultiple -> 1L
                        config.maxSelectionCount > 0 -> config.maxSelectionCount.toLong()
                        else -> 0L // 0 means unlimited
                    }

                    phConfig.filter = when {
                        hasImage && hasVideo -> PHPickerFilter.anyFilterMatchingSubfilters(
                            listOf(PHPickerFilter.imagesFilter, PHPickerFilter.videosFilter)
                        )
                        hasVideo -> PHPickerFilter.videosFilter
                        else -> PHPickerFilter.imagesFilter
                    }

                    val delegate = PHPickerDelegate(onResult)
                    delegateRefs.add(delegate)

                    val picker = PHPickerViewController(configuration = phConfig)
                    picker.delegate = delegate
                    rootVC.presentViewController(picker, animated = true, completion = null)
                }
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberCameraLauncher(
    context: MediaPickerContext,
    captureType: MediaType,
    onResult: (MediaFile?) -> Unit
): ICameraLauncher {
    val delegateRefs = remember { mutableListOf<Any>() }

    return remember(captureType) {
        object : ICameraLauncher {
            override fun launch() {
                val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
                    ?: return

                if (!UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)) {
                    // Camera not available (e.g., simulator) — return null gracefully
                    onResult(null)
                    return
                }

                delegateRefs.clear()

                val delegate = UIImagePickerDelegate(onResult)
                delegateRefs.add(delegate)

                val picker = UIImagePickerController()
                picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                picker.mediaTypes = when (captureType) {
                    MediaType.VIDEO -> listOf(UTTypeMovie.identifier)
                    else -> listOf(UTTypeImage.identifier)
                }
                picker.delegate = delegate
                rootVC.presentViewController(picker, animated = true, completion = null)
            }
        }
    }
}

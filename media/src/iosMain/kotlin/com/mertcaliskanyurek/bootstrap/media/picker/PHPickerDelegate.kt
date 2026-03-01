package com.mertcaliskanyurek.bootstrap.media.picker

import com.mertcaliskanyurek.bootstrap.media.model.MediaFile
import com.mertcaliskanyurek.bootstrap.media.model.MediaType
import com.mertcaliskanyurek.bootstrap.media.utils.toMediaFile
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.UniformTypeIdentifiers.UTTypeMovie
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
internal class PHPickerDelegate(
    private val onResult: (List<MediaFile>) -> Unit
) : NSObject(), PHPickerViewControllerDelegateProtocol {

    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
        picker.dismissViewControllerAnimated(true, completion = null)

        @Suppress("UNCHECKED_CAST")
        val results = didFinishPicking as List<PHPickerResult>

        if (results.isEmpty()) {
            onResult(emptyList())
            return
        }

        val files = mutableListOf<MediaFile>()
        val remaining = kotlin.concurrent.AtomicInt(results.size)

        results.forEach { result ->
            val provider = result.itemProvider
            val typeIdentifier = when {
                provider.hasItemConformingToTypeIdentifier(UTTypeMovie.identifier) -> UTTypeMovie.identifier
                else -> UTTypeImage.identifier
            }
            val mediaType = if (typeIdentifier == UTTypeMovie.identifier) MediaType.VIDEO else MediaType.IMAGE

            provider.loadFileRepresentationForTypeIdentifier(typeIdentifier) { url: NSURL?, _: NSError? ->
                url?.toMediaFile(mediaType)?.let { files.add(it) }
                if (remaining.decrementAndGet() == 0) {
                    onResult(files)
                }
            }
        }
    }
}

package com.mertcaliskanyurek.bootstrap.media.picker

import com.mertcaliskanyurek.bootstrap.media.model.MediaFile
import com.mertcaliskanyurek.bootstrap.media.model.MediaType
import com.mertcaliskanyurek.bootstrap.media.utils.toMediaFile
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
internal class UIDocumentPickerDelegate(
    private val onResult: (List<MediaFile>) -> Unit
) : NSObject(), UIDocumentPickerDelegateProtocol {

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        @Suppress("UNCHECKED_CAST")
        val urls = didPickDocumentsAtURLs as List<NSURL>
        onResult(urls.map { it.toMediaFile(MediaType.FILE) })
    }

    override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
        onResult(emptyList())
    }
}

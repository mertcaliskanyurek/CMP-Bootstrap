package com.mertcaliskanyurek.bootstrap.media.camera

import com.mertcaliskanyurek.bootstrap.media.model.MediaFile
import com.mertcaliskanyurek.bootstrap.media.model.MediaType
import com.mertcaliskanyurek.bootstrap.media.utils.toMediaFile
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerMediaURL
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
internal class UIImagePickerDelegate(
    private val onResult: (MediaFile?) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        picker.dismissViewControllerAnimated(true, completion = null)

        // Try video URL first
        val videoUrl = didFinishPickingMediaWithInfo[UIImagePickerControllerMediaURL] as? NSURL
        if (videoUrl != null) {
            onResult(videoUrl.toMediaFile(MediaType.VIDEO))
            return
        }

        // Otherwise treat as photo
        val image = (didFinishPickingMediaWithInfo[UIImagePickerControllerEditedImage]
            ?: didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage]) as? UIImage

        if (image != null) {
            val uniqueName = NSUUID().UUIDString
            val tmpPath = NSTemporaryDirectory() + "camera_capture_$uniqueName.jpg"
            val jpegData = UIImageJPEGRepresentation(image, 0.9)
            if (jpegData != null) {
                val written = NSFileManager.defaultManager.createFileAtPath(
                    path = tmpPath,
                    contents = jpegData,
                    attributes = null
                )
                if (written) {
                    val fileUrl = NSURL(fileURLWithPath = tmpPath)
                    onResult(fileUrl.toMediaFile(MediaType.IMAGE))
                } else {
                    onResult(null)
                }
            } else {
                onResult(null)
            }
        } else {
            onResult(null)
        }
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion = null)
        onResult(null)
    }
}

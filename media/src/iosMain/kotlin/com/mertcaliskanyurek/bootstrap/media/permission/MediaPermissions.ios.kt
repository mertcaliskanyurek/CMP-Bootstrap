package com.mertcaliskanyurek.bootstrap.media.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.mertcaliskanyurek.bootstrap.media.model.MediaType
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType

@OptIn(ExperimentalForeignApi::class)
private fun currentCameraAuthStatus(): PermissionStatus {
    return when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
        AVAuthorizationStatusAuthorized -> PermissionStatus.GRANTED
        AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> PermissionStatus.DENIED
        AVAuthorizationStatusNotDetermined -> PermissionStatus.NOT_DETERMINED
        else -> PermissionStatus.NOT_DETERMINED
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberCameraPermissionState(
    onResult: (granted: Boolean) -> Unit
): IPermissionState {
    var permissionStatus by remember { mutableStateOf(currentCameraAuthStatus()) }
    val scope = rememberCoroutineScope()

    return remember {
        object : IPermissionState {
            override val status: PermissionStatus
                get() = permissionStatus

            override fun launchPermissionRequest() {
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted: Boolean ->
                    scope.launch(Dispatchers.Main) {
                        permissionStatus = if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED
                        onResult(granted)
                    }
                }
            }
        }
    }
}

/**
 * Gallery access via PHPickerViewController requires no permission on iOS.
 * Always returns [PermissionStatus.GRANTED] and [launchPermissionRequest] is a no-op.
 */
@Composable
actual fun rememberGalleryPermissionState(
    mediaTypes: Set<MediaType>,
    onResult: (granted: Boolean) -> Unit
): IPermissionState = remember {
    object : IPermissionState {
        override val status: PermissionStatus = PermissionStatus.GRANTED
        override fun launchPermissionRequest() { onResult(true) }
    }
}

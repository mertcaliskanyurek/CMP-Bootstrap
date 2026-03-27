package com.mertcaliskanyurek.bootstrap.media.permission

import androidx.compose.runtime.Composable
import com.mertcaliskanyurek.bootstrap.media.model.MediaType

@Composable
expect fun rememberCameraPermissionState(
    onResult: (granted: Boolean) -> Unit = {}
): IPermissionState

@Composable
expect fun rememberGalleryPermissionState(
    mediaTypes: Set<MediaType> = setOf(MediaType.IMAGE),
    onResult: (granted: Boolean) -> Unit = {}
): IPermissionState

package com.mertcaliskanyurek.bootstrap.media.picker

import androidx.compose.runtime.Composable
import com.mertcaliskanyurek.bootstrap.media.model.MediaFile
import com.mertcaliskanyurek.bootstrap.media.model.MediaPickerConfig
import com.mertcaliskanyurek.bootstrap.media.model.MediaType

@Composable
expect fun rememberMediaPickerLauncher(
    context: MediaPickerContext,
    config: MediaPickerConfig,
    onResult: (List<MediaFile>) -> Unit
): IMediaPickerLauncher

@Composable
expect fun rememberCameraLauncher(
    context: MediaPickerContext,
    captureType: MediaType,
    onResult: (MediaFile?) -> Unit
): ICameraLauncher

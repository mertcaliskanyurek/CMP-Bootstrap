package com.mertcaliskanyurek.bootstrap.media.picker

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberMediaPickerContext(): MediaPickerContext =
    MediaPickerContext(LocalContext.current)

package com.mertcaliskanyurek.bootstrap.media.camera

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

internal object AndroidCameraImpl {

    private const val AUTHORITY_SUFFIX = ".cmpbootstrap.mediapicker.provider"

    fun createTempImageUri(context: Context): Uri {
        val dir = File(context.cacheDir, "media_picker").apply { mkdirs() }
        val file = File.createTempFile("img_", ".jpg", dir)
        return FileProvider.getUriForFile(context, context.packageName + AUTHORITY_SUFFIX, file)
    }

    fun createTempVideoUri(context: Context): Uri {
        val dir = File(context.cacheDir, "media_picker").apply { mkdirs() }
        val file = File.createTempFile("vid_", ".mp4", dir)
        return FileProvider.getUriForFile(context, context.packageName + AUTHORITY_SUFFIX, file)
    }
}

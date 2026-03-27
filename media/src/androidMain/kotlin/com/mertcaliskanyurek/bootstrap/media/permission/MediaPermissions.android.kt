package com.mertcaliskanyurek.bootstrap.media.permission

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mertcaliskanyurek.bootstrap.media.model.MediaType

private fun Context.findActivity(): Activity? {
    var ctx: Context = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

private fun checkSinglePermissionStatus(
    context: Context,
    permission: String
): PermissionStatus {
    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        return PermissionStatus.GRANTED
    }
    val activity = context.findActivity()
    if (activity != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        return PermissionStatus.SHOULD_SHOW_RATIONALE
    }
    return PermissionStatus.DENIED
}

private fun checkMultiplePermissionsStatus(
    context: Context,
    permissions: List<String>
): PermissionStatus {
    if (permissions.isEmpty()) return PermissionStatus.GRANTED
    val allGranted = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
    if (allGranted) return PermissionStatus.GRANTED
    val activity = context.findActivity()
    if (activity != null && permissions.any {
        ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
    }) return PermissionStatus.SHOULD_SHOW_RATIONALE
    return PermissionStatus.DENIED
}

@Composable
actual fun rememberCameraPermissionState(
    onResult: (granted: Boolean) -> Unit
): IPermissionState {
    val context = LocalContext.current
    var permissionStatus by remember {
        mutableStateOf(checkSinglePermissionStatus(context, android.Manifest.permission.CAMERA))
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionStatus = if (granted) {
            PermissionStatus.GRANTED
        } else {
            checkSinglePermissionStatus(context, android.Manifest.permission.CAMERA)
        }
        onResult(granted)
    }

    return remember {
        object : IPermissionState {
            override val status: PermissionStatus
                get() = permissionStatus

            override fun launchPermissionRequest() {
                launcher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }
}

@Composable
actual fun rememberGalleryPermissionState(
    mediaTypes: Set<MediaType>,
    onResult: (granted: Boolean) -> Unit
): IPermissionState {
    val context = LocalContext.current

    val permissions: List<String> = remember(mediaTypes) {
        when {
            Build.VERSION.SDK_INT >= 33 -> buildList {
                if (MediaType.IMAGE in mediaTypes) add(android.Manifest.permission.READ_MEDIA_IMAGES)
                if (MediaType.VIDEO in mediaTypes) add(android.Manifest.permission.READ_MEDIA_VIDEO)
            }
            Build.VERSION.SDK_INT in 29..32 -> emptyList() // PickVisualMedia — no permission needed
            else -> listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    var permissionStatus by remember(permissions) {
        mutableStateOf(checkMultiplePermissionsStatus(context, permissions))
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val allGranted = results.values.all { it }
        permissionStatus = if (allGranted) {
            PermissionStatus.GRANTED
        } else {
            checkMultiplePermissionsStatus(context, permissions)
        }
        onResult(allGranted)
    }

    return remember {
        object : IPermissionState {
            override val status: PermissionStatus
                get() = permissionStatus

            override fun launchPermissionRequest() {
                if (permissions.isEmpty()) {
                    onResult(true)
                    return
                }
                launcher.launch(permissions.toTypedArray())
            }
        }
    }
}

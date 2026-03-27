package com.mertcaliskanyurek.bootstrap.media.permission

interface IPermissionState {
    val status: PermissionStatus
    fun launchPermissionRequest()
}

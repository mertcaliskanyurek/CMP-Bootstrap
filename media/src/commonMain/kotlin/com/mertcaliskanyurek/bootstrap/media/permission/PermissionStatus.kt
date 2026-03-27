package com.mertcaliskanyurek.bootstrap.media.permission

enum class PermissionStatus {
    GRANTED,
    DENIED,                  // Permanently denied
    SHOULD_SHOW_RATIONALE,   // Android: user denied once, should explain
    NOT_DETERMINED           // iOS: never asked
}

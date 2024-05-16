package com.mimo.android.apis.devices.window

data class PutWindowRequest(
    val windowId: Long,
    val nickname: String,
    val openDegree: Long,
    val isAccessible: Boolean
)
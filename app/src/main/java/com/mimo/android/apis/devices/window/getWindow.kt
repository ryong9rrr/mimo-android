package com.mimo.android.apis.devices.window

data class GetWindowResponse(
    val windowId: Long,
    val nickname: String,
    val macAddress: String,
    val openDegree: Long,
    val isAccessible: Boolean
)
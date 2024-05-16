package com.mimo.android.apis.devices.light

data class PutLightRequest(
    val lightId: Long,
    val nickname: String,
    val wakeupColor: String,
    val curColor: String,
    val isAccessible: Boolean
)
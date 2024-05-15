package com.mimo.android.apis.devices.light

data class GetLightResponse(
    val lightId: Long,
    val nickname: String,
    val wakeupColor: String,
    val curColor: String,
    val macAddress: String,
    val isAccessible: Boolean
)
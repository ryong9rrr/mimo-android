package com.mimo.android.apis.devices.lamp

data class GetLampResponse(
    val lampId: Long,
    val nickname: String,
    val wakeupColor: String,
    val curColor: String,
    val macAddress: String,
    val isAccessible: Boolean
)
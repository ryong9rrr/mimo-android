package com.mimo.android.apis.devices.lamp

data class PutLampRequest(
    val lampId: Long,
    val nickname: String,
    val wakeupColor: String,
    val curColor: String,
    val isAccessible: Boolean
)
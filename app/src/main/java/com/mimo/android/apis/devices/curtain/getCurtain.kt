package com.mimo.android.apis.devices.curtain

data class GetCurtainResponse(
    val curtainId: Long,
    val nickname: String,
    val macAddress: String,
    val openDegree: Long,
    val isAccessible: Boolean
)
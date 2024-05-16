package com.mimo.android.apis.devices.curtain

data class PutCurtainRequest(
    val curtainId: Long,
    val nickname: String,
    val openDegree: Long,
    val isAccessible: Boolean
)
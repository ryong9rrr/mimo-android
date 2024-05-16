package com.mimo.android.apis.users

data class PostAccessTokenRequest(
    val accessToken: String
)
data class PostAccessTokenResponse(
    val accessToken: String
)

data class GetMyInfoResponse(
    val userId: Int,
    val hasHome: Boolean,
    val hasHub: Boolean
)

data class WakeupTime(
    val hour: Long,
    val minute: Long,
    val second: Long,
    val nano: Long
)
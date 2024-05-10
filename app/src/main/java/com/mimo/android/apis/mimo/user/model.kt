package com.mimo.android.apis.mimo.user

data class PostAccessTokenRequest(
    val accessToken: String
)
data class PostAccessTokenResponse(
    val accessToken: String
)

data class GetMyInfoRequest(
    val accessToken: String
)

data class GetMyInfoResponse(
    val userId: Int,
    val hasHome: Boolean,
    val hasHub: Boolean
)

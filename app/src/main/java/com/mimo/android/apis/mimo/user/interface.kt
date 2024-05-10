package com.mimo.android.apis.mimo.user

import retrofit2.Call
import retrofit2.http.*

interface UserApiService {

    @Headers("Content-Type: application/json")
    @POST("auth")
    fun postAccessToken(
        @Body accessTokenRequest: PostAccessTokenRequest
    ): Call<PostAccessTokenResponse>

    @Headers("Content-Type: application/json")
    @GET("user/myInfo")
    fun getMyInfo(
        @Header("X-AUTH-TOKEN") accessToken: String
    ): Call<GetMyInfoResponse>
}
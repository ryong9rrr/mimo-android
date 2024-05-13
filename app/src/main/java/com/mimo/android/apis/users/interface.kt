package com.mimo.android.apis.users

import retrofit2.Call
import retrofit2.http.*

interface UsersApiService {

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
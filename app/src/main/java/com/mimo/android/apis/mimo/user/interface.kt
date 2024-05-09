package com.mimo.android.apis.mimo.user

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApiService {

    @FormUrlEncoded
    @POST("/auth")
    fun postAccessToken(
        @Field("accessToken") accessToken: String
    ): Call<PostAccessTokenResponse>

}
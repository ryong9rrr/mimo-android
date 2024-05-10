package com.mimo.android.apis.mimo.user

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApiService {

    @Headers("Content-Type: application/json")
    @POST("auth")
    fun postAccessToken(
        @Body accessTokenRequest: PostAccessTokenRequest
    ): Call<PostAccessTokenResponse>

}
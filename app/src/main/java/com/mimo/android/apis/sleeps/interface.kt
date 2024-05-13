package com.mimo.android.apis.sleeps

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface SleepsApiService {
    @Headers("Content-Type: application/json")
    @POST("sleep-data")
    fun postSleepData(
        @Header("X-AUTH-TOKEN") accessToken: String,
        @Body postSleepDataRequest: PostSleepDataRequest
    ): Call<String>
}
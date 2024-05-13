package com.mimo.android.apis.houses

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface HousesApiService {
    @Headers("Content-Type: application/json")
    @POST("houses/new")
    fun postRegisterHouse(
        @Header("X-AUTH-TOKEN") accessToken: String,
        @Body postRegisterHouseRequest: PostRegisterHouseRequest
    ): Call<PostRegisterHouseResponse>

    @Headers("Content-Type: application/json")
    @POST("houses")
    fun postAutoRegisterHubToHouse(
        @Header("X-AUTH-TOKEN") accessToken: String,
        @Body postRegisterHubToHouseRequest: PostAutoRegisterHubToHouseRequest
    ): Call<PostAutoRegisterHubToHouseResponse>

    @Headers("Content-Type: application/json")
    @GET("houses")
    fun getHouseList(
        @Header("X-AUTH-TOKEN") accessToken: String,
    ): Call<List<House>>
}
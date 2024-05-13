package com.mimo.android.apis.mimo.houses

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface HousesApiService {
    @Headers("Content-Type: application/json")
    @POST("houses")
    fun postRegisterHouse(
        @Header("X-AUTH-TOKEN") accessToken: String,
        @Body postRegisterHouseRequest: PostRegisterHouseRequest
    ): Call<PostRegisterHouseResponse>

    // TODO: Any 타입 변경 해야함
    @Headers
    @GET
    fun getHouseList(
        @Header("X-AUTH-TOKEN") accessToken: String,
    ): Call<Any>
}
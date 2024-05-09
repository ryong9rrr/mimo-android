package com.mimo.android.apis.common

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

inline fun <reified ApiService> createApiService(baseUrl: String): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl) // 예시 API baseURL
        .addConverterFactory(GsonConverterFactory.create()) // JSON 파서 지정
        .build()
    return retrofit.create(ApiService::class.java)
}
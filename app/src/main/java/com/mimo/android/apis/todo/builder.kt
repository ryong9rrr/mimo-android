package com.mimo.android.apis.todo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://jsonplaceholder.typicode.com"

private fun createTodoApiService(): ApiService{
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // 예시 API baseURL
        .addConverterFactory(GsonConverterFactory.create()) // JSON 파서 지정
        .build()
    return retrofit.create(ApiService::class.java)
}

val todoApiService = createTodoApiService()
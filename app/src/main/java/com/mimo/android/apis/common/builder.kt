package com.mimo.android.apis.common

import com.mimo.android.apis.todo.TodoApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// TODO: env 처리
private const val TODO_BASE_URL = "https://jsonplaceholder.typicode.com"

inline fun <reified ApiService> createApiService(baseUrl: String): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl) // 예시 API baseURL
        .addConverterFactory(GsonConverterFactory.create()) // JSON 파서 지정
        .build()
    return retrofit.create(ApiService::class.java)
}

val todoApiService = createApiService<TodoApiService>(TODO_BASE_URL)
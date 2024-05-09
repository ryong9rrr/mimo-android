package com.mimo.android.apis.todo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/posts/{postId}") // API 엔드포인트
    fun getPost(
        @Path("postId") postId: String
    ): Call<Post> // GET 요청

    @GET("/posts")
    fun getPostList(): Call<List<Post>>
}
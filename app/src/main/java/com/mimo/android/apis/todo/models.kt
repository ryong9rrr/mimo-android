package com.mimo.android.apis.todo

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
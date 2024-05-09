package com.mimo.android.apis.todo

import retrofit2.Call

fun getPost(
    postId: String,
    onSuccessCallback: ((post: Post?) -> Unit)? = null,
    onFailureCallback: (() -> Unit)? = null
) {
    val call = todoApiService.getPost(postId)
    call.enqueue(object : retrofit2.Callback<Post> {
        override fun onResponse(call: Call<Post>, response: retrofit2.Response<Post>) {
            if (response.isSuccessful) {
                val post = response.body()
                onSuccessCallback?.invoke(post)
            } else {
                onFailureCallback?.invoke()
            }
        }

        override fun onFailure(call: Call<Post>, t: Throwable) {
            println("Network request failed.")
            onFailureCallback?.invoke()
        }
    })
}

fun getPosts(
    onSuccessCallback: ((post: List<Post>?) -> Unit)? = null,
    onFailureCallback: (() -> Unit)? = null
) {
    val call = todoApiService.getPostList()
    call.enqueue(object : retrofit2.Callback<List<Post>> {
        override fun onResponse(call: Call<List<Post>>, response: retrofit2.Response<List<Post>>) {
            if (response.isSuccessful) {
                val posts = response.body()
                onSuccessCallback?.invoke(posts)
            } else {
                onFailureCallback?.invoke()
            }
        }

        override fun onFailure(call: Call<List<Post>>, t: Throwable) {
            println("Network request failed.")
            onFailureCallback?.invoke()
        }
    })
}
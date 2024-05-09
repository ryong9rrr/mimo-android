package com.mimo.android.apis.mimo.user

import retrofit2.Call
import com.mimo.android.apis.common.OnResponseSuccessCallback
import com.mimo.android.apis.common.onResponseFailureCallback
import com.mimo.android.apis.mimo.mimoApiService

fun postAccessToken(
    accessToken: String,
    onSuccessCallback: (OnResponseSuccessCallback<PostAccessTokenResponse>)? = null,
    onFailureCallback: (onResponseFailureCallback)? = null
){
    val call = mimoApiService.postAccessToken(accessToken)
    call.enqueue(object : retrofit2.Callback<PostAccessTokenResponse> {
        override fun onResponse(call: Call<PostAccessTokenResponse>, response: retrofit2.Response<PostAccessTokenResponse>) {
            println(response.code())
            println(response.message())
            if (response.isSuccessful) {
                onSuccessCallback?.invoke(response.body())
            } else {
                onFailureCallback?.invoke()
            }
        }

        override fun onFailure(call: Call<PostAccessTokenResponse>, t: Throwable) {
            println("Network request failed.")
            onFailureCallback?.invoke()
        }
    })
}
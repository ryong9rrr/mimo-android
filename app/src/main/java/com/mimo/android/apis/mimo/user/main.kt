package com.mimo.android.apis.mimo.user

import android.util.Log
import retrofit2.Call
import com.mimo.android.apis.common.OnResponseSuccessCallback
import com.mimo.android.apis.common.onResponseFailureCallback
import com.mimo.android.apis.mimo.mimoApiService

const val TAG = "/apis/mimo/user/main.postAccessToken"

fun postAccessToken(
    accessToken: String,
    onSuccessCallback: (OnResponseSuccessCallback<PostAccessTokenResponse>)? = null,
    onFailureCallback: (onResponseFailureCallback)? = null
){
    val call = mimoApiService.postAccessToken(PostAccessTokenRequest(accessToken))
    call.enqueue(object : retrofit2.Callback<PostAccessTokenResponse> {
        override fun onResponse(call: Call<PostAccessTokenResponse>, response: retrofit2.Response<PostAccessTokenResponse>) {
            Log.e(TAG, response.toString())
            if (response.isSuccessful) {
                onSuccessCallback?.invoke(response.body())
            } else {
                onFailureCallback?.invoke()
            }
        }

        override fun onFailure(call: Call<PostAccessTokenResponse>, t: Throwable) {
            Log.e(TAG, "Network request failed")
            onFailureCallback?.invoke()
        }
    })
}
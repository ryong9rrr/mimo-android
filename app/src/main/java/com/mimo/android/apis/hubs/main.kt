package com.mimo.android.apis.hubs

import android.util.Log
import retrofit2.Call
import com.mimo.android.apis.common.OnResponseSuccessCallback
import com.mimo.android.apis.common.onResponseFailureCallback
import com.mimo.android.apis.mimoApiService

const val TAG = "/apis/mimo/hubs"

fun postRegisterHubToHouse(
    accessToken: String,
    postRegisterHubToHomeRequest: PostRegisterHubToHouseRequest,
    onSuccessCallback: (OnResponseSuccessCallback<Any>)? = null,
    onFailureCallback: (onResponseFailureCallback)? = null
){
    val call = mimoApiService.postRegisterHubToHouse(
        accessToken = accessToken,
        postRegisterHubToHomeRequest = postRegisterHubToHomeRequest
    )
    call.enqueue(object : retrofit2.Callback<Any> {
        override fun onResponse(call: Call<Any>, response: retrofit2.Response<Any>) {
            Log.i(TAG, response.toString())
            if (response.isSuccessful) {
                onSuccessCallback?.invoke(response.body())
            } else {
                onFailureCallback?.invoke()
            }
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            Log.e(TAG, "Network request failed")
            onFailureCallback?.invoke()
        }
    })
}
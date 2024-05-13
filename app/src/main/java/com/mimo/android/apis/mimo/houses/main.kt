package com.mimo.android.apis.mimo.houses

import android.util.Log
import com.mimo.android.apis.common.OnResponseSuccessCallback
import com.mimo.android.apis.common.onResponseFailureCallback
import com.mimo.android.apis.mimo.mimoApiService
import retrofit2.Call

private const val TAG = "apis/houses/main"

fun postRegisterHouse(
    accessToken: String,
    postRegisterHouseRequest: PostRegisterHouseRequest,
    onSuccessCallback: (OnResponseSuccessCallback<PostRegisterHouseResponse>)? = null,
    onFailureCallback: (onResponseFailureCallback)? = null
){
    val call = mimoApiService.postRegisterHouse(
        accessToken = accessToken,
        postRegisterHouseRequest = postRegisterHouseRequest
    )
    call.enqueue(object : retrofit2.Callback<PostRegisterHouseResponse> {
        override fun onResponse(call: Call<PostRegisterHouseResponse>, response: retrofit2.Response<PostRegisterHouseResponse>) {
            Log.i(TAG, response.toString())
            if (response.isSuccessful) {
                onSuccessCallback?.invoke(response.body())
            } else {
                onFailureCallback?.invoke()
            }
        }

        override fun onFailure(call: Call<PostRegisterHouseResponse>, t: Throwable) {
            Log.e(TAG, "Network request failed")
            onFailureCallback?.invoke()
        }
    })
}

fun postAutoRegisterHubToHouse(
    accessToken: String,
    postRegisterHubToHouseRequest: PostAutoRegisterHubToHouseRequest,
    onSuccessCallback: (OnResponseSuccessCallback<PostAutoRegisterHubToHouseResponse>)? = null,
    onFailureCallback: (onResponseFailureCallback)? = null
){
    val call = mimoApiService.postAutoRegisterHubToHouse(
        accessToken = accessToken,
        postRegisterHubToHouseRequest = postRegisterHubToHouseRequest
    )
    call.enqueue(object : retrofit2.Callback<PostAutoRegisterHubToHouseResponse> {
        override fun onResponse(call: Call<PostAutoRegisterHubToHouseResponse>, response: retrofit2.Response<PostAutoRegisterHubToHouseResponse>) {
            Log.i(TAG, response.toString())
            if (response.isSuccessful) {
                onSuccessCallback?.invoke(response.body())
            } else {
                onFailureCallback?.invoke()
            }
        }

        override fun onFailure(call: Call<PostAutoRegisterHubToHouseResponse>, t: Throwable) {
            Log.e(TAG, "Network request failed")
            onFailureCallback?.invoke()
        }
    })
}

fun getHouseList(
    accessToken: String,
    onSuccessCallback: (OnResponseSuccessCallback<Any>)? = null,
    onFailureCallback: (onResponseFailureCallback)? = null
){
    val call = mimoApiService.getHouseList(accessToken = accessToken)
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
package com.mimo.android.apis.hubs

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.mimo.android.apis.common.OnResponseSuccessCallback
import com.mimo.android.apis.common.onResponseFailureCallback
import com.mimo.android.apis.mimoApiService
import retrofit2.Call

private const val TAG = "apis/hubs/getHubListByHouseId"

fun getHubListByHouseId(
    accessToken: String,
    houseId: Long,
    onSuccessCallback: (OnResponseSuccessCallback<List<Hub>>)? = null,
    onFailureCallback: (onResponseFailureCallback)? = null
){
    val call = mimoApiService.getHubListByHouseId(
        accessToken = accessToken,
        houseId = houseId
    )
    call.enqueue(object : retrofit2.Callback<List<Hub>> {
        override fun onResponse(call: Call<List<Hub>>, response: retrofit2.Response<List<Hub>>) {
            if (response.isSuccessful) {
                onSuccessCallback?.invoke(response.body())
            } else {
                onFailureCallback?.invoke()
            }
        }

        override fun onFailure(call: Call<List<Hub>>, t: Throwable) {
            Log.e(TAG, "Network request failed")
            onFailureCallback?.invoke()
        }
    })
}

data class Hub(
    val id: Long,
    val registeredDttm: String,
    val unregisteredDttm: String,
    val nickname: String,
    val house: House,
    val serialNumber: String,
    val lamp: DeviceLamp,
    val light: List<DeviceLight>,
    val slidingWindow: List<DeviceSlidingWindow>,
    val curtain: List<DeviceCurtain>,
    val registered: Boolean
)

data class House(
    val id: Long,
    val registeredDttm: String,
    val unregisteredDttm: String,
    val address: String,
    val userHouse: List<UserHouse>,
    val hub: List<String>,
    val active: Boolean
)

data class UserHouse(
    val id: Long,
    val registeredDttm: String,
    val unregisteredDttm: String,
    val nickname: String,
    val user: User,
    val house: String,
    val home: Boolean,
    val active: Boolean
)

data class User(
    val id: Long,
    val registeredDttm: String,
    val unregisteredDttm: String,
    val keyCode: String,
    val roles: List<String>,
    val isSuperUser: Boolean,
    val email: String,
    val nickname: String,
    val wakeupTime: WakeupTime,
    val userHouse: List<String>,
    val password: String,
    val enabled: Boolean,
    val username: String,
    val authorities: List<Authority>,
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val credentialsNonExpired: Boolean,
    val active: Boolean
)

data class WakeupTime(
    val hour: Long,
    val minute: Long,
    val second: Long,
    val nano: Long
)

data class Authority(
    val authority: String
)

data class DeviceLamp(
    val id: Long,
    val registeredDttm: String,
    val unregisteredDttm: String,
    val nickname: String,
    val user: User,
    val hub: String,
    val macAddress: String,
    val wakeupColor: String,
    val curColor: String,
    val accessible: Boolean,
    val registered: Boolean
)

data class DeviceLight(
    val id: Long,
    val registeredDttm: String,
    val unregisteredDttm: String,
    val nickname: String,
    val user: User,
    val hub: String,
    val macAddress: String,
    val wakeupColor: String,
    val curColor: String,
    val accessible: Boolean,
    val registered: Boolean
)

data class DeviceSlidingWindow(
    val id: Long,
    val registeredDttm: String,
    val unregisteredDttm: String,
    val nickname: String,
    val user: User,
    val hub: String,
    val macAddress: String,
    val openDegree: Long,
    val accessible: Boolean,
    val registered: Boolean
)

data class DeviceCurtain(
    val id: Long,
    val registeredDttm: String,
    val unregisteredDttm: String,
    val nickname: String,
    val user: User,
    val hub: String,
    val macAddress: String,
    val openDegree: Long,
    val accessible: Boolean,
    val registered: Boolean
)
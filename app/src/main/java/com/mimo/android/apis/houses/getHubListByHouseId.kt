package com.mimo.android.apis.houses
import android.util.Log
import com.google.gson.annotations.SerializedName
import com.mimo.android.apis.common.OnResponseSuccessCallback
import com.mimo.android.apis.common.onResponseFailureCallback
import com.mimo.android.apis.mimoApiService
import retrofit2.Call

private const val TAG = "apis/houses/getHubListByHouseId"

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
    val id: Int,
    @SerializedName("registeredDttm") val registeredDateTime: String,
    @SerializedName("unregisteredDttm") val unregisteredDateTime: String,
    val nickname: String,
    val house: RegisteredHouse,
    val serialNumber: String,
    val lamp: List<Lamp>,
    val light: List<Light>,
    val slidingWindow: List<SlidingWindow>,
    val curtain: List<Curtain>,
    val registered: Boolean
)

data class RegisteredHouse(
    val id: Int,
    @SerializedName("registeredDttm") val registeredDateTime: String,
    @SerializedName("unregisteredDttm") val unregisteredDateTime: String,
    val address: String,
    val userHouse: List<UserHouse>,
    val hub: List<String>,
    val active: Boolean
)

data class WakeUpTime(
    val hour: Long,
    val minute: Long,
    val second: Long,
    val nano: Long,
)

data class User (
    val id: Long,
    @SerializedName("registeredDttm") val registeredDateTime: String,
    @SerializedName("unregisteredDttm") val unregisteredDateTime: String,
    val isSuperUser: Boolean,
    val nickname: String,
    val wakeupTime: WakeUpTime,
    val userHouse: List<String>,
    val username: String,
)

data class UserHouse(
    val id: Int,
    @SerializedName("registeredDttm") val registeredDateTime: String,
    @SerializedName("unregisteredDttm") val unregisteredDateTime: String,
    val nickname: String,
    val user: User,
    val house: String,
    val home: Boolean,
    val active: Boolean
)

data class Lamp(
    val id: Int,
    @SerializedName("registeredDttm") val registeredDateTime: String,
    @SerializedName("unregisteredDttm") val unregisteredDateTime: String,
    val nickname: String,
    val user: User,
    val hub: String,
    val macAddress: String,
    val wakeupColor: String,
    val curColor: String,
    val accessible: Boolean,
    val registered: Boolean
)

data class Light(
    val id: Int,
    @SerializedName("registeredDttm") val registeredDateTime: String,
    @SerializedName("unregisteredDttm") val unregisteredDateTime: String,
    val nickname: String,
    val user: User,
    val hub: String,
    val macAddress: String,
    val wakeupColor: String,
    val curColor: String,
    val accessible: Boolean,
    val registered: Boolean
)

data class SlidingWindow(
    val id: Int,
    @SerializedName("registeredDttm") val registeredDateTime: String,
    @SerializedName("unregisteredDttm") val unregisteredDateTime: String,
    val nickname: String,
    val user: User,
    val hub: String,
    val macAddress: String,
    val openDegree: Int,
    val accessible: Boolean,
    val registered: Boolean
)

data class Curtain(
    val id: Int,
    @SerializedName("registeredDttm") val registeredDateTime: String,
    @SerializedName("unregisteredDttm") val unregisteredDateTime: String,
    val nickname: String,
    val user: User,
    val hub: String,
    val macAddress: String,
    val openDegree: Int,
    val accessible: Boolean,
    val registered: Boolean
)
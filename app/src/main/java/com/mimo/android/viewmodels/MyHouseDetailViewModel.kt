package com.mimo.android.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.android.apis.houses.Device
import com.mimo.android.apis.houses.GetDeviceListByHouseIdResponse
import com.mimo.android.apis.houses.getDeviceListByHouseId
import com.mimo.android.apis.hubs.getHubListByHouseId
import com.mimo.android.utils.alertError
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.USER_ID
import com.mimo.android.utils.preferences.getData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "MyHouseDetailViewModel"

class MyHouseDetailViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MyHouseDetailUiState())
    val uiState: StateFlow<MyHouseDetailUiState> = _uiState.asStateFlow()

    fun getDevices(): Devices{
        val _userId = getData(USER_ID)
        val myDeviceList = mutableListOf<Device>()
        val anotherDeviceList = mutableListOf<Device>()

        if (_uiState.value.house == null || _userId.isNullOrEmpty()) {
            return Devices(myDeviceList = myDeviceList, anotherDeviceList = anotherDeviceList)
        }

        val userId = _userId.toLong()
        val allDeviceList = _uiState.value.house!!.devices
        for (device in allDeviceList) {
            if (device.userId == userId) {
                myDeviceList.add(device)
            } else {
                anotherDeviceList.add(device)
            }
        }
        return Devices(myDeviceList = myDeviceList, anotherDeviceList = anotherDeviceList)
    }

    fun fetchGetDeviceListByHouseId(houseId: Long){
        viewModelScope.launch {
            getDeviceListByHouseId(
                accessToken = getData(ACCESS_TOKEN) ?: "",
                houseId = houseId,
                onSuccessCallback = { data: GetDeviceListByHouseIdResponse? ->
                    if (data == null) {
                        alertError()
                        return@getDeviceListByHouseId
                    }
                    _uiState.value = MyHouseDetailUiState(house = data)
                },
                onFailureCallback = {
                    Log.e(TAG, "fetchGetDeviceListByHouseId")
                    alertError()
                }
            )
        }
    }
}

data class MyHouseDetailUiState(
    val house: GetDeviceListByHouseIdResponse? = null
)

data class Devices(
    val myDeviceList: List<Device>,
    val anotherDeviceList: List<Device>,
)
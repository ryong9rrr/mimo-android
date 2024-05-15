package com.mimo.android.viewmodels

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.android.apis.houses.Device
import com.mimo.android.apis.houses.GetDeviceListByHouseIdResponse
import com.mimo.android.apis.houses.getDeviceListByHouseId
import com.mimo.android.apis.hubs.getHubListByHouseId
import com.mimo.android.components.devices.fakeGetMyDeviceList
import com.mimo.android.utils.alertError
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.USER_ID
import com.mimo.android.utils.preferences.getData
import com.mimo.android.utils.showToast
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

    fun queryDevice(
        deviceId: Long,
        deviceType: DeviceType
    ): Device? {
        val myDeviceList = getDevices().myDeviceList

        if (deviceType == DeviceType.CURTAIN) {
            return myDeviceList.find { _device -> isCurtainType(_device.type) && _device.deviceId == deviceId }
        }

        if (deviceType == DeviceType.LAMP) {
            return myDeviceList.find { _device -> isLampType(_device.type) && _device.deviceId == deviceId }
        }

        if (deviceType == DeviceType.LIGHT) {
            return myDeviceList.find { _device -> isLightType(_device.type) && _device.deviceId == deviceId }
        }

        if (deviceType == DeviceType.WINDOW) {
            return myDeviceList.find { _device -> isWindowType(_device.type) && _device.deviceId == deviceId }
        }

        return null
    }

    fun fetchGetDeviceListByHouseId(houseId: Long){
        // TODO: 더미를 진짜로..
        _uiState.value = MyHouseDetailUiState(house = GetDeviceListByHouseIdResponse(
            devices = fakeGetMyDeviceList(),
            houseId = 1,
            nickname = "더미더미",
            address = "더미 주소",
            isHome = true,
        ))
        return

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

    fun fetchToggleMyDevice(deviceId: Long){
        // TODO: 디바이스 토글, 디바이스의 현재 상태가 정의되어있지 않은 것 같다...
        showToast("${deviceId} 토글함!")
    }
}

data class MyHouseDetailUiState(
    val house: GetDeviceListByHouseIdResponse? = null
)

data class Devices(
    val myDeviceList: List<Device>,
    val anotherDeviceList: List<Device>,
)

enum class DeviceType {
    WINDOW,
    CURTAIN,
    LIGHT,
    LAMP
}

fun isCurtainType(x: String): Boolean{
    return x == "커튼" || x.toLowerCase() == "curtain"
}

fun isLightType(x: String): Boolean{
    return x == "조명" || x.toLowerCase() == "light"
}

fun isLampType(x: String): Boolean{
    return x == "무드등" || x.toLowerCase() == "lamp"
}

fun isWindowType(x: String): Boolean{
    return x == "창문" || x.toLowerCase() == "window" || x.toLowerCase() == "slidingwindow"
}

fun convertDeviceTypeToKoreaName(x: String): String {
    if (isCurtainType(x)) {
        return "커튼"
    }
    if (isLightType(x)) {
        return "조명"
    }
    if (isLampType(x)) {
        return "무드등"
    }
    if (isWindowType(x)) {
        return "창문"
    }
    return ""
}
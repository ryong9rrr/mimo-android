package com.mimo.android.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.android.apis.houses.GetDeviceListByHouseIdResponse
import com.mimo.android.apis.houses.getDeviceListByHouseId
import com.mimo.android.apis.houses.getHubListByHouseId
import com.mimo.android.utils.alertError
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.getData
import com.mimo.android.utils.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "viewmodels/MyHomeDetailViewModel"

class MyHouseDetailViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MyHouseDetailUiState())
    val uiState: StateFlow<MyHouseDetailUiState> = _uiState.asStateFlow()

    fun fetchHubListByHouseId(houseId: Long){
        viewModelScope.launch {
            getHubListByHouseId(
                accessToken = getData(ACCESS_TOKEN) ?: "",
                houseId = houseId,
                onSuccessCallback = { hubList ->
                    Log.i(TAG, hubList?.toString() ?: "[]")
                },
                onFailureCallback = {
                    Log.e(TAG, "fetchHubListByHouseId")
                    alertError()
                }
            )
        }
    }

    fun fetchGetDeviceListByHouseId(houseId: Long){
        viewModelScope.launch {
            getDeviceListByHouseId(
                accessToken = getData(ACCESS_TOKEN) ?: "",
                houseId = houseId,
                onSuccessCallback = { data: GetDeviceListByHouseIdResponse? ->
                    Log.i(TAG, data.toString())
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
    val hubList: List<Hub> =  mutableListOf(),
    val house: GetDeviceListByHouseIdResponse? = null
)
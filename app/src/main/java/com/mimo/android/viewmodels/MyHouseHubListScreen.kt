package com.mimo.android.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.android.apis.hubs.Hub
import com.mimo.android.apis.hubs.getHubListByHouseId
import com.mimo.android.utils.alertError
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.getData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "MyHouseHubListViewModel"

class MyHouseHubListViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MyHouseHubListUiState())
    val uiState: StateFlow<MyHouseHubListUiState> = _uiState.asStateFlow()

    fun fetchHubListByHouseId(houseId: Long){
        viewModelScope.launch {
            getHubListByHouseId(
                accessToken = getData(ACCESS_TOKEN) ?: "",
                houseId = houseId,
                onSuccessCallback = { hubList ->
                    _uiState.value = MyHouseHubListUiState(
                        hubList = hubList ?: mutableListOf()
                    )
                },
                onFailureCallback = {
                    Log.e(TAG, "fetchHubListByHouseId")
                    alertError()
                }
            )
        }
    }
}

data class MyHouseHubListUiState(
    val hubList: List<Hub>? = null
)
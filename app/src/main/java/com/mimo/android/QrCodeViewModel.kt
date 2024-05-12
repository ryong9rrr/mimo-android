package com.mimo.android

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.android.apis.mimo.hubs.PostRegisterHubToHouseRequest
import com.mimo.android.apis.mimo.hubs.postRegisterHubToHouse
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.getData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "QrCodeViewModel"

class QrCodeViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(QrCodeUiState())
    val uiState: StateFlow<QrCodeUiState> = _uiState.asStateFlow()

    fun init(qrCode: String){
        _uiState.value = QrCodeUiState(qrCode = qrCode)
    }

    fun registerHubToHouse(houseId: Long){
        viewModelScope.launch {
            val qrCode = _uiState.value.qrCode
            if (qrCode == null) {
                Toast.makeText(
                    MainActivity.getMainActivityContext(),
                    "다시 시도해주세요",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "QR Code가 초기화되지 않음")
                return@launch
            }

            val accessToken = getData(ACCESS_TOKEN)
            if (accessToken == null) {
                Log.e(TAG, "accessToken이 없음")
                return@launch
            }

            postRegisterHubToHouse(
                accessToken = accessToken,
                postRegisterHubToHomeRequest = PostRegisterHubToHouseRequest(
                    serialNumber = qrCode,
                    houseId = houseId
                ),
                onSuccessCallback = {
                    Toast.makeText(
                        MainActivity.getMainActivityContext(),
                        "허브를 등록했어요",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onFailureCallback = {
                    Toast.makeText(
                        MainActivity.getMainActivityContext(),
                        "다시 시도해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    // 초기세팅을 하는 경우 (이 경우 집 정보는 나중에 받음)
    fun setFirstSetting(qrCode: String){
        _uiState.value = QrCodeUiState(qrCode = qrCode)
    }

    // 허브를 집에 등록하는 경우
    fun setHubToHome(){

    }

    // 기기를 허브에 등록하는 경우

    fun removeQrCode(){
        _uiState.value = QrCodeUiState(qrCode = null)
    }
}

data class QrCodeUiState (
    val qrCode: String? = null,
    val selectedHouseId: Long? = null,
    val selectedHubSerialNumber: String? = null,
    val selectedMachineId: Long? = null
)
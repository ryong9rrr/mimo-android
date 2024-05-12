package com.mimo.android

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QrCodeViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(QrCodeUiState())
    val uiState: StateFlow<QrCodeUiState> = _uiState.asStateFlow()

    fun init(qrCode: String){
        _uiState.value = QrCodeUiState(qrCode = qrCode)
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
    val selectedHomeId: String? = null,
    val selectedHubId: String? = null,
    val selectedMachineId: String? = null
)
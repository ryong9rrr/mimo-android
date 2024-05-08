package com.mimo.android

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QrCodeViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(QrCodeUiState())
    val uiState: StateFlow<QrCodeUiState> = _uiState.asStateFlow()

    fun updateQrCode(qrCode: String){
        _uiState.value = QrCodeUiState(qrCode = qrCode)
    }

    fun removeQrCode(){
        _uiState.value = QrCodeUiState(qrCode = null)
    }
}

data class QrCodeUiState (
    val qrCode: String? = null
)
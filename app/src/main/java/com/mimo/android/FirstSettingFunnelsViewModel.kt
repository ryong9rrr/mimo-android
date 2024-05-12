package com.mimo.android

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FirstSettingFunnelsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(FirstSettingFunnelsUiState())
    val uiState: StateFlow<FirstSettingFunnelsUiState> = _uiState.asStateFlow()

    fun init(currentStepId: Int){
        viewModelScope.launch {
            delay(1000)
            _uiState.update {
                prevState -> prevState.copy(currentStepId = currentStepId)
            }
        }
    }

    fun updateCurrentStep(stepId: Int){
        _uiState.update {
            prevState -> prevState.copy(currentStepId = stepId)
        }
    }

    fun setHubAndRedirect(
        qrCode: String,
        launchGoogleLocationAndAddress: (cb: (userLocation: UserLocation?) -> Unit) -> Unit
    ){
        // TODO: 유효한 QR코드인지 확인
        // TODO: 샬라샬라...

        // if (유효하지 않은 코드라면) ... 샬라샬라...

        // TODO: 아무튼 유효한 코드라면...
        viewModelScope.launch {
            delay(3000)
            // TODO: Loading UI State

            // TODO: 여기서 Spring server 통해서 허브(QR)이미 존재하는지 아닌지 확인하고 리다이렉트
            // Case1 이미 집이 등록된 허브라서 메인으로 이동
            var isExistingHub = true
            if (isExistingHub) {
                // TODO: 여기서는 이미 존재하는 허브이기 때문에 유저를 등록시키는 API를 호출하고 잠깐 화면만 보여줌.
                val responseLocation = "경기도 고양시 일산서구 산현로 34"
                val responseLocationAlias = "상윤이의 본가"

                // 등록시킨 후 이동, first_setting_redirect_main_after_find_existing_hub에서는 메인으로 리다이렉트하게 됨.
                _uiState.update { prevState ->
                    prevState.copy(
                        currentStepId = R.string.first_setting_redirect_main_after_find_existing_hub,
                        hub = Hub(
                            qrCode = qrCode,
                            location = responseLocation,
                            locationAlias = responseLocationAlias
                        )
                    )
                }
                return@launch
            }

            // Case2 새로운 허브라서 주소 등록 화면으로 이동
            launchGoogleLocationAndAddress { userLocation ->
                _uiState.update { prevState ->
                    prevState.copy(
                        userLocation = userLocation,
                        currentStepId = R.string.first_setting_funnel_auto_register_location
                    )
                }
            }
        }
    }

    fun redirectMain(){
        viewModelScope.launch {
            delay(3000)
            // TODO: Loading UI State

            _uiState.update {
                    prevState -> prevState.copy(currentStepId = null, hub = null)
            }
        }
    }

//    fun redirectAutoRegisterLocationFunnel(userLocation: UserLocation?){
//        viewModelScope.launch {
//            // TODO: Loading UI State
//            delay(2000)
//            _uiState.update { prevState ->
//                prevState.copy(
//                    currentStepId = R.string.first_setting_funnel_auto_register_location,
//                    userLocation = userLocation
//                )
//            }
//        }
//    }
}

data class FirstSettingFunnelsUiState (
    val currentStepId: Int? = null,
    val hub: Hub? = null,
    val userLocation: UserLocation? = null,
)

data class Hub(
    val qrCode: String?,
    val location: String?,
    val locationAlias: String?
)

data class UserLocation(
    val location: Location? = null,
    val address: String? = null
)
package com.mimo.android

import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.android.apis.mimo.houses.*
import com.mimo.android.apis.mimo.hubs.PostRegisterHubToHouseRequest
import com.mimo.android.apis.mimo.hubs.postRegisterHubToHouse
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.getData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "FirstSettingFunnelsViewModel"

class FirstSettingFunnelsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(FirstSettingFunnelsUiState())
    val uiState: StateFlow<FirstSettingFunnelsUiState> = _uiState.asStateFlow()

    fun updateCurrentStep(stepId: Int){
        _uiState.update {
            prevState -> prevState.copy(currentStepId = stepId)
        }
    }

    fun setHubAndRedirect(
        qrCode: String,
        launchGoogleLocationAndAddress: (cb: (userLocation: UserLocation?) -> Unit) -> Unit
    ){
        viewModelScope.launch {
            delay(3000)
            // TODO: Loading UI State

            postAutoRegisterHubToHouse(
                accessToken = getData(ACCESS_TOKEN) ?: "",
                postRegisterHubToHouseRequest = PostAutoRegisterHubToHouseRequest(
                    serialNumber = qrCode
                ),
                onSuccessCallback = { data: PostAutoRegisterHubToHouseResponse? ->
                    if (data == null) {
                        alertError()
                        return@postAutoRegisterHubToHouse
                    }

                    if (data.address == null && data.houseId == null) {
                        // Case2 새로운 허브라서 주소 등록 화면으로 이동
                        launchGoogleLocationAndAddress { userLocation ->
                            _uiState.update { prevState ->
                                prevState.copy(
                                    userLocation = userLocation,
                                    currentStepId = R.string.first_setting_funnel_auto_register_location
                                )
                            }
                        }
                        return@postAutoRegisterHubToHouse
                    }

                    // Case1 이미 집이 등록된 허브라서 메인으로 이동
                    // 등록시킨 후 이동, first_setting_redirect_main_after_find_existing_hub에서는 메인으로 리다이렉트하게 됨.
                    _uiState.update { prevState ->
                        prevState.copy(
                            currentStepId = R.string.first_setting_redirect_main_after_find_existing_hub,
                            hub = Hub(
                                serialNumber = qrCode,
                                address = data.address,
                                houseId = data.houseId
                            )
                        )
                    }
                },
                onFailureCallback = {
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
            )
        }
    }

    fun registerNewHubAndRedirectToMain(
        qrCode: String
    ){
        viewModelScope.launch {
            val address = _uiState.value.userLocation?.address
            if (address == null) {
                alertError()
                return@launch
            }

            postRegisterHouse(
                accessToken = getData(ACCESS_TOKEN)!!,
                postRegisterHouseRequest = PostRegisterHouseRequest(
                    address = address,
                    nickname = address
                ),
                onSuccessCallback = { data: PostRegisterHouseResponse? ->
                    if (data == null) {
                        alertError()
                        return@postRegisterHouse
                    }

                    postRegisterHubToHouse(
                        accessToken = getData(ACCESS_TOKEN)!!,
                        postRegisterHubToHomeRequest = PostRegisterHubToHouseRequest(
                            serialNumber = qrCode,
                            houseId = data.houseId
                        ),
                        onSuccessCallback = { data ->
                            Log.i(TAG, "허브 ${qrCode}를 $address 에 등록완료!!")
                            redirectToMain()
                        },
                        onFailureCallback = {
                            Log.e(TAG, "postRegisterHubToHouse에서 실패")
                            alertError()
                        }
                    )
                },
                onFailureCallback = {
                    Log.e(TAG, "postRegisterHouse에서 실패")
                    alertError()
                }
            )
        }
    }

    fun redirectToMain(){
        viewModelScope.launch {
            delay(3000)
            // TODO: Loading UI State
            _uiState.value = FirstSettingFunnelsUiState()
        }
    }

    private fun alertError(){
        Toast.makeText(
            MainActivity.getMainActivityContext(),
            "오류가 발생했습니다",
            Toast.LENGTH_SHORT
        ).show()
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
    val serialNumber: String? = null,
    val address: String? = null,
    val houseId: Long? = null
)

data class UserLocation(
    val location: Location? = null,
    val address: String? = null
)
package com.mimo.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.android.apis.mimo.user.GetMyInfoResponse
import com.mimo.android.apis.mimo.user.getMyInfo
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.getData
import com.mimo.android.utils.preferences.removeData
import com.mimo.android.utils.preferences.saveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun init(
        firstSettingFunnelsViewModel: FirstSettingFunnelsViewModel
    ){
        val prevAccessToken = getData(ACCESS_TOKEN) ?: return

        viewModelScope.launch {
            getMyInfo(
                accessToken = prevAccessToken,
                onSuccessCallback = { data: GetMyInfoResponse? ->
                    if (data == null) {
                        return@getMyInfo
                    }

                    if (!data.hasHome && !data.hasHub) {
                        firstSettingFunnelsViewModel.updateCurrentStep(R.string.first_setting_funnel_first_setting_start)
                    }

                    _uiState.update { prevState ->
                        prevState.copy(
                            accessToken = prevAccessToken,
                            user = User(
                                id = data.userId.toString(),
                                hasHome = data.hasHome,
                                hasHub = data.hasHub)
                        )
                    }
                }
            )
        }
    }

    fun login(
        accessToken: String,
        firstSettingFunnelsViewModel: FirstSettingFunnelsViewModel
    ){
        viewModelScope.launch {
            saveData(ACCESS_TOKEN, accessToken)
            getMyInfo(
                accessToken = accessToken,
                onSuccessCallback = { data: GetMyInfoResponse? ->
                    if (data == null) {
                        return@getMyInfo
                    }

                    if (!data.hasHome && !data.hasHub) {
                        firstSettingFunnelsViewModel.updateCurrentStep(R.string.first_setting_funnel_first_setting_start)
                    }

                    _uiState.update { prevState ->
                        prevState.copy(
                            accessToken = accessToken,
                            user = User(
                                id = data.userId.toString(),
                                hasHome = data.hasHome,
                                hasHub = data.hasHub)
                            )
                    }
                }
            )
        }
    }

    fun logout(){
        //removeData(ACCESS_TOKEN)
        _uiState.update { prevState ->
            prevState.copy(user = null)
        }
    }
}

data class AuthUiState(
    val user: User? = null,
    val accessToken: String? = null
)

data class User(
    val id: String,
    val hasHome: Boolean,
    val hasHub: Boolean
)
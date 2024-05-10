package com.mimo.android

import android.util.Log
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

    fun isLoggedIn(): Boolean {
        return _uiState.value.accessToken != null
    }

    // 집과 허브 둘 다 없다면 초기 설정 시작
    fun needFirstSetting(): Boolean {
        if (_uiState.value.user == null) {
            Log.e(TAG, "user가 없는데 이 함수 needFirstSetting()를 호출함. needFirstSetting()은 user를 세팅한 후 호출해야함.")
            return false
        }
        return !_uiState.value.user!!.hasHome && !_uiState.value.user!!.hasHub
    }

    fun init(
        cb: (() -> Unit)?
    ){
        viewModelScope.launch {
            val accessToken = getData(ACCESS_TOKEN)

            if (accessToken == null) {
                _uiState.update { prevState ->
                    prevState.copy(appLoading = false)
                }
                return@launch
            }

            Log.i("AuthViewModel", "디바이스가 가지고 있는 accessToken : $accessToken")
            saveData(ACCESS_TOKEN, accessToken)
            _uiState.update { prevState ->
                prevState.copy(accessToken = accessToken)
            }
            // TODO: "내 정보 줘" API 호출
            fetchGetMyInfo(
                accessToken = accessToken,
                cb = cb
            )
        }
    }

    fun login(
        accessToken: String? = null,
        cb: (() -> Unit)? = null
    ){
        viewModelScope.launch {
            if (accessToken == null) {
                return@launch
            }
            println("login")
            saveData(ACCESS_TOKEN, accessToken)
            // TODO: "내 정보 줘" API 호출
            fetchGetMyInfo(
                accessToken = accessToken,
                cb = cb
            )
        }
    }

    fun finishLoading(){
        _uiState.update { prevState ->
            prevState.copy(appLoading = false)
        }
    }

    private fun fetchGetMyInfo(
        accessToken: String,
        cb: (() -> Unit)?
    ){
        getMyInfo(
            accessToken = accessToken,
            onSuccessCallback = { data: GetMyInfoResponse? ->

                if (data != null) {
                    val fetchedUserId = data.userId
                    val fetchedHasHome = data.hasHome
                    val fetchedHasHub = data.hasHub
                    val fetchedUser = User(
                        id = fetchedUserId.toString(),
                        hasHome = fetchedHasHome,
                        hasHub = fetchedHasHub
                    )
                    _uiState.update { prevState ->
                        prevState.copy(user = fetchedUser)
                    }
                    cb?.invoke()
                }
            },
            onFailureCallback = {
                Log.e(TAG, "fetchUserInfo 실패..")
            }
        )
//        return withContext(Dispatchers.IO) {
//            Thread.sleep(1000)
//            println("fetchGetMyInfo")
//            val fetchedUser = User(
//                id = "asfcvasrvse",
//                name = "상윤",
//                hasHome = false,
//                hasHub = false
//            )
//            _uiState.update { prevState ->
//                prevState.copy(user = fetchedUser)
//            }
//            cb?.invoke()
//        }
    }

    fun logout(){
        removeData(ACCESS_TOKEN)
        _uiState.update { prevState ->
            prevState.copy(accessToken = null, user = null)
        }
    }
}

data class AuthUiState(
    val appLoading: Boolean = true,
    val user: User? = null,
    val accessToken: String? = null
)

data class User(
    val id: String,
    val hasHome: Boolean,
    val hasHub: Boolean
)

data class GetMyInfoResponse(
    val userId: String,
    val hasHome: Boolean,
    val hasHub: Boolean
)
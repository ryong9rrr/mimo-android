package com.mimo.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.getData
import com.mimo.android.utils.preferences.saveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun isLoggedIn(): Boolean{
        return _uiState.value.accessToken != null
    }

    // 집과 허브 둘 다 없다면 초기 설정 시작
    fun needFirstSetting(): Boolean {
        if (_uiState.value.user == null) {
            throw Exception("user가 없는데 이 함수 needFirstSetting()를 호출함. needFirstSetting()은 user를 세팅한 후 호출해야함.")
        }
        return !_uiState.value.user!!.hasHome && !_uiState.value.user!!.hasHub
    }

    fun init(){
        val accessToken = getData(ACCESS_TOKEN) ?: return
        _uiState.update { prevState ->
            prevState.copy(accessToken = accessToken)
        }
        fetchUser()
    }

    fun login(
        accessToken: String?
    ){
        if (accessToken == null) {
            return
        }
        saveData(ACCESS_TOKEN, accessToken)
        _uiState.update { prevState ->
            prevState.copy(accessToken = accessToken)
        }
    }

    fun logout(){
        _uiState.update { prevState ->
            prevState.copy(accessToken = null, user = null)
        }
    }

    private fun fetchUser(){
        viewModelScope.launch {
            // TODO: "내 정보 줘" API 호출
            delay(1000)
            val fetchedUser = User(
                id = "asfcvasrvse",
                name = "상윤",
                hasHome = false,
                hasHub = false
            )
            _uiState.update { prevState ->
                prevState.copy(user = fetchedUser)
            }
        }
    }
}

data class AuthUiState(
    val user: User? = null,
    var accessToken: String? = null
)

data class User(
    val id: String,
    val name: String,
    val hasHome: Boolean,
    val hasHub: Boolean
)
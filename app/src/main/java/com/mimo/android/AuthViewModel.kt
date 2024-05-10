package com.mimo.android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.getData
import com.mimo.android.utils.preferences.removeData
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

    fun isLoggedIn(): Boolean {
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
        fetchInit(accessToken)
    }

    fun login(
        accessToken: String? = null,
        cb: (() -> Unit)? = null
    ){
        if (accessToken == null) {
            return
        }
        println("login")
        saveData(ACCESS_TOKEN, accessToken)
        fetchInit(
            accessToken = accessToken,
            cb = cb
        )
    }

    fun logout(){
        removeData(ACCESS_TOKEN)
        _uiState.update { prevState ->
            prevState.copy(accessToken = null, user = null)
        }
    }

    private fun fetchInit(
        accessToken: String,
        cb: (() -> Unit)? = null
    ){
        viewModelScope.launch {
            println("fetchInit")
            // TODO: "내 정보 줘" API 호출
            delay(1000)
            val fetchedUser = User(
                id = "asfcvasrvse",
                name = "상윤",
                hasHome = false,
                hasHub = false
            )
            _uiState.update { prevState ->
                prevState.copy(accessToken = accessToken, user = fetchedUser)
            }
            cb?.invoke()
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
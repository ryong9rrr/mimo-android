package com.mimo.android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun init(user: User?){
        // TODO: jwt-token이 있는지 확인하고 없으면 not-login 처리 / 있으면 그걸 가지고 백엔드에 요청해서 확인

        // TODO: 1. jwt-token이 있는가?

        // TODO: 2. jwt-token이 있으면 백엔드에 요청
        viewModelScope.launch {
            delay(1000)
            if (user != null) {
                _uiState.update { prevState ->
                    prevState.copy(user = user)
                }
            }
        }
    }

    fun login(
        user: User,
        cb: (() -> Unit)? = null
    ){
        viewModelScope.launch {
            delay(1000)
            try {
                _uiState.value = AuthUiState(user = user)
                cb?.invoke()
            } catch(e: Exception) {
                Log.d("AuthViewModel login function error", "${e.message}")
            }
        }
    }

    fun logout(){
        _uiState.value = AuthUiState(user = null)
    }
}

data class AuthUiState(
    val user: User? = null
)

data class User(
    val username: String,
    val accessToken: String,
    val refreshToken: String
)
package com.mimo.android.screens.main.myhome

import androidx.lifecycle.ViewModel
import com.mimo.android.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyHomeViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MyHomeUiState())
    val uiState: StateFlow<MyHomeUiState> = _uiState.asStateFlow()

    fun updateCurrentHome(currentHome: Home?){
        _uiState.update { prevState -> prevState.copy(currentHome = currentHome) }
    }

    fun updateAnotherHomeList(anotherHomeList: Array<Home>?) {
        _uiState.update { prevState -> prevState.copy(anotherHomeList = anotherHomeList) }
    }
}

data class MyHomeUiState(
    val currentHome: Home? = null,
    val anotherHomeList: Array<Home>? = null
)

data class Home(
    val homeId: String? = null,
    val items: Array<String>? = null,
    val homeName: String,
    val address: String
)
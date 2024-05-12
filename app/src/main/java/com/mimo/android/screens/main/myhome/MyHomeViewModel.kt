package com.mimo.android.screens.main.myhome

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.android.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyHomeViewModel: ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var context: Context? = null
    private val _uiState = MutableStateFlow(MyHomeUiState())
    val uiState: StateFlow<MyHomeUiState> = _uiState.asStateFlow()

    fun isCurrentHome(homeId: Long?): Boolean {
        if (_uiState.value.currentHome == null) {
            return false
        }
        return _uiState.value.currentHome!!.homeId == homeId
    }

    fun getHome(homeId: Long?): Home?{
        if (homeId == null || _uiState.value.currentHome == null) {
            return null
        }

        if (_uiState.value.currentHome!!.homeId == homeId) {
            return _uiState.value.currentHome
        }
        return _uiState.value.anotherHomeList.find { it.homeId == homeId }
    }

    fun updateCurrentHome(currentHome: Home?){
        _uiState.update { prevState -> prevState.copy(currentHome = currentHome) }
    }

    fun updateAnotherHomeList(anotherHomeList: List<Home>) {
        _uiState.update { prevState -> prevState.copy(anotherHomeList = anotherHomeList) }
    }

    fun changeCurrentHome(anotherHomeId: Long?){
        if (_uiState.value.currentHome == null || anotherHomeId == null) {
            return
        }

        if (_uiState.value.currentHome!!.homeId == anotherHomeId) {
            Toast.makeText(
                this.context,
                "이미 현재 거주지에요.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewModelScope.launch {
            // TODO : 현재 거주지 변경 API 호출
            val nextCurrentHome = _uiState.value.copy().anotherHomeList.find { it.homeId == anotherHomeId }
            var nextAnotherHomeList = _uiState.value.copy().anotherHomeList.filter { it.homeId != anotherHomeId }
            nextAnotherHomeList = nextAnotherHomeList.plus(_uiState.value.copy().currentHome!!)
            nextAnotherHomeList = nextAnotherHomeList.sortedBy { home -> home.homeId }
            _uiState.update { prevState ->
                prevState.copy(
                    currentHome = nextCurrentHome,
                    anotherHomeList = nextAnotherHomeList
                )
            }
            Toast.makeText(
                MainActivity.getMainActivityContext(),
                "현재 거주지를 변경했어요",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

data class MyHomeUiState(
    val currentHome: Home? = null,
    val anotherHomeList: List<Home> = mutableListOf()
)

data class Home(
    val homeId: Long? = null,
    val items: Array<String>? = null,
    val homeName: String,
    val address: String
)
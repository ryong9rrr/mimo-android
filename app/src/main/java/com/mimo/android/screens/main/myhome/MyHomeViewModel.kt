package com.mimo.android.screens.main.myhome

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyHomeViewModel: ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var context: Context? = null
    private val _uiState = MutableStateFlow(MyHomeUiState())
    val uiState: StateFlow<MyHomeUiState> = _uiState.asStateFlow()

    fun init(context: Context){
        this.context = context
    }

    fun isCurrentHome(homeId: String?): Boolean {
        if (_uiState.value.currentHome == null) {
            return false
        }
        return _uiState.value.currentHome!!.homeId == homeId
    }

    fun getHome(homeId: String?): Home?{
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

    fun changeCurrentHome(anotherHomeId: String?){
        if (_uiState.value.currentHome == null) {
            return
        }

        val nextCurrentHome = _uiState.value.anotherHomeList.find { it.homeId == anotherHomeId }
        var nextAnotherHomeList = _uiState.value.anotherHomeList.filter { it.homeId != anotherHomeId }
        nextAnotherHomeList = nextAnotherHomeList.plus(_uiState.value.currentHome!!)
        nextAnotherHomeList = nextAnotherHomeList.sortedBy { home -> home.homeId }

        _uiState.value = MyHomeUiState(
            currentHome = nextCurrentHome,
            anotherHomeList = nextAnotherHomeList
        )

        if (this.context != null) {
            Toast.makeText(
                this.context,
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
    val homeId: String? = null,
    val items: Array<String>? = null,
    val homeName: String,
    val address: String
)
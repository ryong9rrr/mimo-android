package com.mimo.android.viewmodels

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.android.MainActivity
import com.mimo.android.apis.houses.House
import com.mimo.android.apis.houses.PostRegisterHouseRequest
import com.mimo.android.apis.houses.getHouseList
import com.mimo.android.apis.houses.postRegisterHouse
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.getData
import com.mimo.android.utils.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyHouseViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MyHouseUiState())
    val uiState: StateFlow<MyHouseUiState> = _uiState.asStateFlow()

    fun updateHouseList(houseList: List<House>){
        _uiState.update { prevState ->
            prevState.copy(houseList = houseList)
        }
    }

    fun getCurrentHouse(
        myHouseUiState: MyHouseUiState,
    ): House? {
        val house = myHouseUiState.houseList.find { house ->
            house.isHome
        }
        return house
    }

    fun getAnotherHouseList(
        myHouseUiState: MyHouseUiState,
    ): List<House> {
        return myHouseUiState.houseList.filter { house ->
            !house.isHome
        }
    }

    fun queryHouse(
        myHouseUiState: MyHouseUiState,
        houseId: Long
    ): House? {
        return myHouseUiState.houseList.find { house -> house.houseId == houseId }
    }

    fun fetchHouseList(){
        viewModelScope.launch {
            getHouseList(
                accessToken = getData(ACCESS_TOKEN) ?: "",
                onSuccessCallback = { houses ->
                    if (houses == null) {
                        showToast("집 목록을 불러오지 못했어요")
                        return@getHouseList
                    }
                    _uiState.update { prevState ->
                        prevState.copy(houseList = houses)
                    }
                },
                onFailureCallback = {
                    showToast("집 목록을 불러오지 못했어요")
                }
            )
        }
    }

    fun fetchCreateNewHouse(
        address: String,
        onSuccessCallback: (() -> Unit)? = null,
        onFailureCallback: (() -> Unit)? = null,
    ){
        viewModelScope.launch {
            postRegisterHouse(
                accessToken = getData(ACCESS_TOKEN) ?: "",
                postRegisterHouseRequest = PostRegisterHouseRequest(
                    address = address,
                    nickname = address
                ),
                onSuccessCallback = { onSuccessCallback?.invoke() },
                onFailureCallback = { onFailureCallback?.invoke() }
            )
        }
    }

    fun fetchChangeHouseNickname(newNickname: String){
        viewModelScope.launch {
            showToast(newNickname)
        }
    }

    fun changeCurrentHouse(house: House){

//        if (_uiState.value.currentHome == null || anotherHomeId == null) {
//            return
//        }
//
//        if (_uiState.value.currentHome!!.homeId == anotherHomeId) {
//            Toast.makeText(
//                this.context,
//                "이미 현재 거주지에요.",
//                Toast.LENGTH_SHORT
//            ).show()
//            return
//        }
//
//        viewModelScope.launch {
//            // TODO : 현재 거주지 변경 API 호출
//            val nextCurrentHome = _uiState.value.copy().anotherHomeList.find { it.homeId == anotherHomeId }
//            var nextAnotherHomeList = _uiState.value.copy().anotherHomeList.filter { it.homeId != anotherHomeId }
//            nextAnotherHomeList = nextAnotherHomeList.plus(_uiState.value.copy().currentHome!!)
//            nextAnotherHomeList = nextAnotherHomeList.sortedBy { home -> home.homeId }
//            _uiState.update { prevState ->
//                prevState.copy(
//                    currentHome = nextCurrentHome,
//                    anotherHomeList = nextAnotherHomeList
//                )
//            }
//            Toast.makeText(
//                MainActivity.getMainActivityContext(),
//                "현재 거주지를 변경했어요",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
    }
}

data class MyHouseUiState(
    val houseList: List<House> = mutableListOf()
)
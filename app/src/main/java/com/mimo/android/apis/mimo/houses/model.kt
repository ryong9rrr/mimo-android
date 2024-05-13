package com.mimo.android.apis.mimo.houses

data class PostRegisterHouseRequest(
    val address: String,
    val nickname: String
)

data class PostRegisterHouseResponse(
    val houseId: Long
)

//data class GetHouseListResponse()
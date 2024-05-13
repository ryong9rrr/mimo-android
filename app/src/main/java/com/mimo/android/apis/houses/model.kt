package com.mimo.android.apis.houses

data class PostRegisterHouseRequest(
    val address: String,
    val nickname: String
)

data class PostRegisterHouseResponse(
    val houseId: Long
)

data class PostAutoRegisterHubToHouseRequest(
    val serialNumber: String
)

data class PostAutoRegisterHubToHouseResponse(
    val houseId: Long?,
    val address: String?
)

//data class GetHouseListResponse()
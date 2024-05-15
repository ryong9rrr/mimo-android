package com.mimo.android.screens

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route : String
}

object MyHouseScreenDestination: Destination {
    override val route = "MyHouseScreenDestination"
}

object MyHouseDetailScreenDestination: Destination {
    override val route = "MyHouseDetailScreenDestination"
    const val houseIdTypeArg = "houseId"
    val routeWithArgs = "$route/{$houseIdTypeArg}"
    val arguments = listOf(
        navArgument(houseIdTypeArg) { type = NavType.StringType }
    )
}

object MyProfileScreenDestination: Destination {
    override val route = "MyProfileScreenDestination"
}

object SleepScreenDestination: Destination {
    override val route = "SleepScreenDestination"
}

object CreateHouseConfirmScreenDestination: Destination {
    override val route = "CreateHouseConfirmScreen"
}

object ChangeHouseNicknameScreenDestination: Destination {
    override val route = "ChangeHouseNicknameScreenDestination"
    const val houseIdTypeArg = "houseId"
    val routeWithArgs = "${route}/{$houseIdTypeArg}"
    val arguments = listOf(
        navArgument(houseIdTypeArg) { type = NavType.StringType }
    )
}

object MyHouseHubListScreenDestination: Destination {
    override val route = "MyHouseHubListScreen"
    const val houseIdTypeArg = "houseId"
    val routeWithArgs = "$route/{$houseIdTypeArg}"
    val arguments = listOf(
        navArgument(houseIdTypeArg) { type = NavType.StringType }
    )
}

object MyHouseSimpleDeviceListDestination: Destination {
    override val route = "MyHouseSimpleDeviceListDestination"
    const val hubIdTypeArg = "hubId"
    val routeWithArgs = "$route/{$hubIdTypeArg}"
    val arguments = listOf(
        navArgument(hubIdTypeArg) { type = NavType.StringType }
    )
}

fun isShowNavigation(currentRoute: String?): Boolean{
    if (currentRoute == null) {
        return true
    }

    if (currentRoute.contains("ChangeHouseNicknameScreenDestination")) {
        return false
    }

    if (currentRoute.contains("CreateHouseConfirmScreen")) {
        return false
    }

    return true
}
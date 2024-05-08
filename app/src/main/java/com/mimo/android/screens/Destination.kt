package com.mimo.android.screens

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route : String
}

object MyHomeDestination: Destination {
    override val route = "main_myhome"
}

object MyHomeDetailDestination: Destination {
    override val route = "main_myhomedetail"
    const val homeIdTypeArg = "homeId"
    val routeWithArgs = "$route/{$homeIdTypeArg}"
    val arguments = listOf(
        navArgument(homeIdTypeArg) { type = NavType.StringType }
    )
}

object MyProfileDestination: Destination {
    override val route = "main_myprofile"
}

object SleepDestination: Destination {
    override val route = "main_sleep"
}
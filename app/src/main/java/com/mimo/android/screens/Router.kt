package com.mimo.android.screens

import com.mimo.android.R
import com.mimo.android.screens.main.myhome.MyHomeScreen
import com.mimo.android.services.health.HealthConnectManager
import com.mimo.android.screens.main.myprofile.MyProfileScreen
import com.mimo.android.screens.main.sleep.SleepScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mimo.android.screens.main.myhome.HubHome

@Composable
fun Router(
    navController: NavHostController,
    healthConnectManager: HealthConnectManager,
//    serviceRunning: Boolean? = null,
//    currentLocation: String? = null,
//    onClickForeground: (() -> Unit)? = null,
){
    NavHost(navController = navController, startDestination = Screen.MyHomeScreen.route) {
        //val availability by healthConnectManager.availability

        // main
        composable(Screen.MyHomeScreen.route) {
            val currentHome = HubHome(
                items = arrayOf("조명", "무드등"),
                homeName = "상윤이의 자취방",
                address = "서울특별시 관악구 봉천동 1234-56"
            )
            val anotherHomes: Array<HubHome> = arrayOf(
                HubHome(
                    items = arrayOf("조명", "창문", "커튼"),
                    homeName = "상윤이의 본가",
                    address = "경기도 고양시 일산서구 산현로12"
                ),
                HubHome(
                    items = arrayOf("조명", "커튼"),
                    homeName = "싸피",
                    address = "서울특별시 강남구 테헤란로 212"
                )
            )

            MyHomeScreen(
                navController = navController,
                currentHome = currentHome,
                anotherHomes = anotherHomes
            )
            return@composable
        }
        composable(Screen.SleepScreen.route) {
            SleepScreen(
                navController = navController
            )
            return@composable
        }
        composable(Screen.MyProfileScreen.route) {
            MyProfileScreen(
                navController = navController,
                healthConnectManager = healthConnectManager
            )
            return@composable
        }


//        composable(Screen.ForegroundServiceSampleScreen.route) {
//            ForegroundServiceSampleScreen(
//                serviceRunning = serviceRunning,
//                currentLocation = currentLocation,
//                onClick = onClickForeground)
//        }
    }
}

enum class Screen(val route: String, val titleId: Int) {
    MyHomeScreen("my_home_screen", R.string.my_home_screen),
    SleepScreen("sleep_screen", R.string.sleep_screen),
    MyProfileScreen("my_profile_screen", R.string.my_profile_screen),
    //ForegroundServiceSampleScreen("foreground_service_sample_screen", R.string.foreground_service_sample_screen)
}
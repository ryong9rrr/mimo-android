package com.mimo.android.screens

import com.mimo.android.screens.main.myhome.MyHomeScreen
import com.mimo.android.services.health.HealthConnectManager
import com.mimo.android.screens.main.myprofile.MyProfileScreen
import com.mimo.android.screens.main.sleep.SleepScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mimo.android.screens.main.myhome.Home
import com.mimo.android.screens.main.myhome.MyHomeDetailScreen
import com.mimo.android.screens.main.myhome.MyHomeViewModel

@Composable
fun Router(
    navController: NavHostController,
    healthConnectManager: HealthConnectManager,
//    serviceRunning: Boolean? = null,
//    currentLocation: String? = null,
//    onClickForeground: (() -> Unit)? = null,
){
    val myHomeViewModel = MyHomeViewModel()
    myHomeViewModel.init(context = LocalContext.current)

    NavHost(navController = navController, startDestination = MyHomeDestination.route) {
        //val availability by healthConnectManager.availability

        // main
        composable(MyHomeDestination.route) {
            val currentHome = Home(
                homeId = "1",
                items = arrayOf("조명", "무드등"),
                homeName = "상윤이의 자취방",
                address = "서울특별시 관악구 봉천동 1234-56"
            )
            val anotherHomeList: List<Home> = mutableListOf(
                Home(
                    homeId = "2",
                    items = arrayOf("조명", "창문", "커튼"),
                    homeName = "상윤이의 본가",
                    address = "경기도 고양시 일산서구 산현로12 경기도 고양시 일산서구 산현로12 경기도 고양시 일산서구 산현로12 경기도 고양시 일산서구 산현로12 경기도 고양시 일산서구 산현로12"
                ),
                Home(
                    homeId = "3",
                    items = arrayOf("조명", "커튼"),
                    homeName = "낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가",
                    address = "서울특별시 강남구 테헤란로 212"
                ),
                Home(
                    homeId = "4",
                    items = arrayOf("조명", "커튼"),
                    homeName = "싸피",
                    address = "서울특별시 강남구 테헤란로 212"
                ),
                Home(
                    homeId = "5",
                    items = arrayOf("조명", "커튼"),
                    homeName = "싸피",
                    address = "서울특별시 강남구 테헤란로 212"
                )
            )

            myHomeViewModel.updateCurrentHome(currentHome)
            myHomeViewModel.updateAnotherHomeList(anotherHomeList)

            MyHomeScreen(
                navController = navController,
                myHomeViewModel = myHomeViewModel
            )
            return@composable
        }
        composable(SleepDestination.route) {
            SleepScreen(
                navController = navController
            )
            return@composable
        }
        composable(MyProfileDestination.route) {
            MyProfileScreen(
                navController = navController,
                healthConnectManager = healthConnectManager
            )
            return@composable
        }
        composable(
            route = MyHomeDetailDestination.routeWithArgs,
            arguments = MyHomeDetailDestination.arguments
        ){ backStackEntry ->
            val homeId = backStackEntry.arguments?.getString(MyHomeDetailDestination.homeIdTypeArg)
            val home = myHomeViewModel.getHome(homeId)
            if (home == null) {
                navController.navigate(MyHomeDestination.route) {
                    popUpTo(0)
                }
                return@composable
            }
            MyHomeDetailScreen(
                navController = navController,
                home = home,
                isCurrentHome = myHomeViewModel.isCurrentHome(home.homeId),
                myItems = Any(),
                anotherPeopleItems = Any()
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
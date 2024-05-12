package com.mimo.android.screens

import com.mimo.android.screens.main.myhome.MyHomeScreen
import com.mimo.android.services.health.HealthConnectManager
import com.mimo.android.screens.main.myprofile.MyProfileScreen
import com.mimo.android.screens.main.sleep.SleepScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mimo.android.screens.main.myhome.MyHomeDetailScreen
import com.mimo.android.screens.main.myhome.MyHomeViewModel

@Composable
fun Router(
    navController: NavHostController,
    isActiveSleepForegroundService: Boolean,
    healthConnectManager: HealthConnectManager,
    onStartSleepForegroundService: (() -> Unit)? = null,
    onStopSleepForegroundService: (() -> Unit)? = null,
    myHomeViewModel: MyHomeViewModel,
    checkCameraPermissionHub: () -> Unit,
    checkCameraPermissionMachine: () -> Unit,
){
    NavHost(navController = navController, startDestination = SleepDestination.route) {
        //val availability by healthConnectManager.availability

        // main
        composable(MyHomeDestination.route) {
            MyHomeScreen(
                navController = navController,
                myHomeViewModel = myHomeViewModel
            )
            return@composable
        }
        composable(SleepDestination.route) {
            SleepScreen(
                navController = navController,
                isActiveSleepForegroundService = isActiveSleepForegroundService,
                onStartSleepForegroundService = onStartSleepForegroundService,
                onStopSleepForegroundService = onStopSleepForegroundService
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
    }
}
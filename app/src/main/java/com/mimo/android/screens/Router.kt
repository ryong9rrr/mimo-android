package com.mimo.android.screens

import com.mimo.android.screens.main.myhome.MyHomeScreen
import com.mimo.android.services.health.HealthConnectManager
import com.mimo.android.screens.main.myprofile.MyProfileScreen
import com.mimo.android.screens.main.sleep.SleepScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mimo.android.AuthViewModel
import com.mimo.android.QrCodeViewModel
import com.mimo.android.screens.main.myhome.*

@Composable
fun Router(
    navController: NavHostController,
    isActiveSleepForegroundService: Boolean,
    healthConnectManager: HealthConnectManager,
    onStartSleepForegroundService: (() -> Unit)? = null,
    onStopSleepForegroundService: (() -> Unit)? = null,
    myHomeViewModel: MyHomeViewModel,
    qrCodeViewModel: QrCodeViewModel,
    checkCameraPermissionHubToHouse: () -> Unit,
    checkCameraPermissionMachineToHub: () -> Unit,
    authViewModel: AuthViewModel
){
    NavHost(navController = navController, startDestination = SleepDestination.route) {
        composable(MyHomeDestination.route) {
            MyHomeScreen(
                navController = navController,
                myHomeViewModel = myHomeViewModel,
                qrCodeViewModel = qrCodeViewModel,
                checkCameraPermissionHubToHouse = checkCameraPermissionHubToHouse,
                checkCameraPermissionMachineToHub = checkCameraPermissionMachineToHub
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
                healthConnectManager = healthConnectManager,
                authViewModel = authViewModel
            )
            return@composable
        }

        composable(
            route = MyHomeDetailDestination.routeWithArgs,
            arguments = MyHomeDetailDestination.arguments
        ){ backStackEntry ->
            val homeId = backStackEntry.arguments?.getString(MyHomeDetailDestination.homeIdTypeArg)
            val home = myHomeViewModel.getHome(homeId?.toLong())
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
                anotherPeopleItems = Any(),
                qrCodeViewModel = qrCodeViewModel,
                checkCameraPermissionHubToHouse = checkCameraPermissionHubToHouse,
                checkCameraPermissionMachineToHub = checkCameraPermissionMachineToHub
            )
            return@composable
        }

        composable(
            route = HomeHubListScreen.routeWithArgs,
            arguments = HomeHubListScreen.arguments
        ){ backStackEntry ->
            val homeId = backStackEntry.arguments?.getString(HomeHubListScreen.homeIdTypeArg)
            val home = myHomeViewModel.getHome(homeId?.toLong())
            if (home == null) {
                navController.navigate(MyHomeDestination.route) {
                    popUpTo(0)
                }
                return@composable
            }
            HomeHubListScreen(
                navController = navController,
                home = home,
            )
            return@composable
        }
    }
}
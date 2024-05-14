package com.mimo.android.screens

import com.mimo.android.services.health.HealthConnectManager
import com.mimo.android.screens.main.myprofile.MyProfileScreen
import com.mimo.android.screens.main.sleep.SleepScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mimo.android.screens.main.myhome.MyHouseHubListScreen
import com.mimo.android.viewmodels.AuthViewModel
import com.mimo.android.viewmodels.QrCodeViewModel
import com.mimo.android.screens.main.myhouse.*
import com.mimo.android.viewmodels.MyHouseDetailViewModel
import com.mimo.android.viewmodels.MyHouseViewModel
import com.mimo.android.viewmodels.UserLocation

@Composable
fun Router(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    isActiveSleepForegroundService: Boolean,
    healthConnectManager: HealthConnectManager,
    onStartSleepForegroundService: (() -> Unit)? = null,
    onStopSleepForegroundService: (() -> Unit)? = null,
    myHouseViewModel: MyHouseViewModel,
    myHouseDetailViewModel: MyHouseDetailViewModel,
    qrCodeViewModel: QrCodeViewModel,
    checkCameraPermissionHubToHouse: () -> Unit,
    checkCameraPermissionMachineToHub: () -> Unit,
    launchGoogleLocationAndAddress: (cb: (userLocation: UserLocation?) -> Unit) -> Unit,
){
    val myHouseUiState by myHouseViewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = SleepScreenDestination.route) {
        composable(MyHouseScreenDestination.route) {
            MyHouseScreen(
                navController = navController,
                myHouseViewModel = myHouseViewModel,
                qrCodeViewModel = qrCodeViewModel,
                checkCameraPermissionHubToHouse = checkCameraPermissionHubToHouse,
                checkCameraPermissionMachineToHub = checkCameraPermissionMachineToHub
            )
            return@composable
        }

        composable(SleepScreenDestination.route) {
            SleepScreen(
                navController = navController,
                isActiveSleepForegroundService = isActiveSleepForegroundService,
                onStartSleepForegroundService = onStartSleepForegroundService,
                onStopSleepForegroundService = onStopSleepForegroundService
            )
            return@composable
        }

        composable(MyProfileScreenDestination.route) {
            MyProfileScreen(
                navController = navController,
                healthConnectManager = healthConnectManager,
                authViewModel = authViewModel
            )
            return@composable
        }

        composable(route = CreateHouseConfirmScreenDestination.route){
            CreateHouseConfirmScreen(
                navController = navController,
                launchGoogleLocationAndAddress = launchGoogleLocationAndAddress,
                myHouseViewModel = myHouseViewModel
            )
            return@composable
        }

        composable(
            route = ChangeHouseNicknameScreenDestination.routeWithArgs,
            arguments = ChangeHouseNicknameScreenDestination.arguments
        ) { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString(ChangeHouseNicknameScreenDestination.houseIdTypeArg)
            val house = myHouseViewModel.queryHouse(myHouseUiState, houseId!!.toLong())
            if (house == null) {
                navController.navigate(ChangeHouseNicknameScreenDestination.route) {
                    popUpTo(0)
                }
                return@composable
            }

            ChangeHouseNicknameScreen(
                navController = navController,
                houseId = house.houseId,
                myHouseViewModel = myHouseViewModel)
        }

        composable(
            route = MyHouseDetailScreenDestination.routeWithArgs,
            arguments = MyHouseDetailScreenDestination.arguments
        ){ backStackEntry ->
            val houseId = backStackEntry.arguments?.getString(MyHouseDetailScreenDestination.houseIdTypeArg)
            val house = myHouseViewModel.queryHouse(myHouseUiState, houseId!!.toLong())
            if (house == null) {
                navController.navigate(MyHouseScreenDestination.route) {
                    popUpTo(0)
                }
                return@composable
            }
            MyHouseDetailScreen(
                navController = navController,
                myHouseDetailViewModel = myHouseDetailViewModel,
                house = house,
//                isCurrentHouse = myHouseViewModel.getCurrentHouse(myHouseUiState)?.id == house.id,
//                myItems = Any(),
//                anotherPeopleItems = Any(),
//                qrCodeViewModel = qrCodeViewModel,
//                checkCameraPermissionHubToHouse = checkCameraPermissionHubToHouse,
//                checkCameraPermissionMachineToHub = checkCameraPermissionMachineToHub
            )
            return@composable
        }

        composable(
            route = MyHouseHubListScreenDestination.routeWithArgs,
            arguments = MyHouseHubListScreenDestination.arguments
        ){ backStackEntry ->
            val houseId = backStackEntry.arguments?.getString(MyHouseHubListScreenDestination.houseIdTypeArg)
            val house = myHouseViewModel.queryHouse(myHouseUiState, houseId!!.toLong())
            if (house == null) {
                navController.navigate(MyHouseScreenDestination.route) {
                    popUpTo(0)
                }
                return@composable
            }
            MyHouseHubListScreen(
                navController = navController,
                house = house,
            )
            return@composable
        }
    }
}
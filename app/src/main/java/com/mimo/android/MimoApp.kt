package com.mimo.android

import androidx.compose.material3.*
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mimo.android.components.BackgroundImage
import com.mimo.android.services.health.HealthConnectManager
import com.mimo.android.screens.*
import com.mimo.android.screens.firstsettingfunnels.*
import com.mimo.android.screens.login.LoginScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MimoApp(
    authViewModel: AuthViewModel,
    qrCodeViewModel: QrCodeViewModel,
    checkCameraPermission: () -> Unit,
    firstSettingFunnelsViewModel: FirstSettingFunnelsViewModel,
    healthConnectManager: HealthConnectManager,
    launchGoogleLocationAndAddress: (cb: (userLocation: UserLocation?) -> Unit) -> Unit,
    context: Context,
//    serviceRunning: Boolean? = null,
//    currentLocation: String? = null,
//    onClickForeground: (() -> Unit)? = null,
    ){
    MaterialTheme {
        val scaffoldState = rememberScaffoldState()
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val availability by healthConnectManager.availability

        val authUiState by authViewModel.uiState.collectAsState()
        val firstSettingFunnelsUiState by firstSettingFunnelsViewModel.uiState.collectAsState()
        val canShowMain = authUiState.user != null && firstSettingFunnelsUiState.currentStepId == null

        // TODO: 실제 kakao-login 구현
        fun handleKakaoLoginMock(){
            val user = User(
                username = "용상윤",
                accessToken = "123",
                refreshToken = "456"
            )

            val hasHomeOrHub = true // FIXME: 있다 치고 메인으로 이동

            // TODO: 로그인이 됐는지 확인하고 로그인이 된 상태이며 집과 허브가 모두 있다면... MainActivity도..
            if (hasHomeOrHub) {
                authViewModel.login(
                    user = user,
                    cb = { navController.navigate(MyHomeDestination.route) }
                )
                return
            }

            firstSettingFunnelsViewModel.init(
                currentStepId = R.string.first_setting_funnel_first_setting_start
            )
//                firstSettingFunnelsViewModel.init(
//                    currentStepId = R.string.test_funnel
//                )

            authViewModel.login(
                user = user
            )
        }

        Scaffold(
            bottomBar = {
                if (canShowMain) {
                    Navigation(navController = navController)
                }
            }
        ) {
            BackgroundImage {
                Box(modifier = Modifier.padding(16.dp)) {
                    if (authUiState.user == null) {
                        LoginScreen(
                            onLoginWithKakao = ::handleKakaoLoginMock
                        )
                        return@BackgroundImage
                    }

                    if (firstSettingFunnelsUiState.currentStepId != null) {
                        FirstSettingFunnelsRoot(
                            qrCodeViewModel = qrCodeViewModel,
                            firstSettingFunnelsViewModel = firstSettingFunnelsViewModel,
                            checkCameraPermission = checkCameraPermission,
                            launchGoogleLocationAndAddress = launchGoogleLocationAndAddress,
                            context = context
                        )
                        return@BackgroundImage
                    }

                    Router(
                        navController = navController,
                        healthConnectManager = healthConnectManager,
//                serviceRunning = serviceRunning,
//                currentLocation = currentLocation,
//                onClickForeground = onClickForeground,
                    )
                }
            }
        }
    }

//    MaterialTheme {
//        Scaffold(
//
//            content = {
//                BackgroundImage {
//                    if (authUiState.user == null) {
//                        Box(modifier = Modifier.padding(16.dp)) {
//                            LoginScreen(
//                                onLoginWithKakao = ::handleKakaoLoginMock
//                            )
//                        }
//                        return@BackgroundImage
//                    }
//
//                    if (firstSettingFunnelsUiState.currentStepId != null) {
//                        Box(modifier = Modifier.padding(16.dp)) {
//                            FirstSettingFunnelsRoot(
//                                qrCodeViewModel = qrCodeViewModel,
//                                firstSettingFunnelsViewModel = firstSettingFunnelsViewModel,
//                                checkCameraPermission = checkCameraPermission,
//                                launchGoogleLocationAndAddress = launchGoogleLocationAndAddress,
//                                context = context
//                            )
//                        }
//                        return@BackgroundImage
//                    }
//
//                    Scaffold (
//                        bottomBar = {
//
//                        },
//                        content = {
//                            Router(
//                                navController = navController,
//                                healthConnectManager = healthConnectManager,
////                serviceRunning = serviceRunning,
////                currentLocation = currentLocation,
////                onClickForeground = onClickForeground,
//                            )
//                        }
//                    )
//                }
//            }
//        )
//    }
}
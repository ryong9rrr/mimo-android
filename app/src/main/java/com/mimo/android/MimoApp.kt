package com.mimo.android

import androidx.compose.material3.*
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mimo.android.apis.users.postAccessToken
import com.mimo.android.components.BackgroundImage
import com.mimo.android.viewmodels.AuthViewModel
import com.mimo.android.viewmodels.FirstSettingFunnelsViewModel
import com.mimo.android.viewmodels.QrCodeViewModel
import com.mimo.android.viewmodels.UserLocation
import com.mimo.android.services.health.HealthConnectManager
import com.mimo.android.screens.*
import com.mimo.android.screens.firstsettingfunnels.*
import com.mimo.android.screens.login.LoginScreen
import com.mimo.android.services.kakao.loginWithKakao
import com.mimo.android.viewmodels.MyHouseDetailViewModel
import com.mimo.android.viewmodels.MyHouseViewModel

private const val TAG = "MimoApp"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MimoApp(
    context: Context,
    isActiveSleepForegroundService: Boolean,
    authViewModel: AuthViewModel,
    qrCodeViewModel: QrCodeViewModel,
    firstSettingFunnelsViewModel: FirstSettingFunnelsViewModel,
    myHouseViewModel: MyHouseViewModel,
    myHouseDetailViewModel: MyHouseDetailViewModel,
    healthConnectManager: HealthConnectManager,
    launchGoogleLocationAndAddress: (cb: (userLocation: UserLocation?) -> Unit) -> Unit,
    onStartSleepForegroundService: (() -> Unit)? = null,
    onStopSleepForegroundService: (() -> Unit)? = null,
    checkCameraPermissionFirstSetting: () -> Unit,
    checkCameraPermissionHubToHouse: () -> Unit,
    checkCameraPermissionMachineToHub: () -> Unit
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

        // TODO: 실제 kakao-login 구현
        fun handleLoginWithKakao(){
            loginWithKakao(
                context = context,
                onSuccessCallback = { oauthToken ->
                    Log.i(TAG, "kakao accessToken=${oauthToken.accessToken}")
                    postAccessToken(
                        accessToken = oauthToken.accessToken,
                        onSuccessCallback = { data ->
                            if (data == null) {
                                Log.e(TAG, "데이터가 없음...")
                                return@postAccessToken
                            }
                            Log.i(TAG, "우리 토큰 받아오기 성공!!!! ${data.accessToken}")
                            authViewModel.login(
                                accessToken = data.accessToken,
                                firstSettingFunnelsViewModel = firstSettingFunnelsViewModel
                            )
                            Toast.makeText(
                                context,
                                "로그인 되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onFailureCallback = {
                            Toast.makeText(
                                context,
                                "다시 로그인 해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                },
                onFailureCallback = {
                    Toast.makeText(
                        context,
                        "카카오 로그인 실패",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }

        Scaffold(
            bottomBar = {
                if (authUiState.user != null && firstSettingFunnelsUiState.currentStepId == null) {
                    Navigation(navController = navController)
                }
            }
        ) {
            BackgroundImage {
                Box(modifier = Modifier.padding(16.dp)) {
                    if (firstSettingFunnelsUiState.currentStepId != null) {
                        FirstSettingFunnelsRoot(
                            qrCodeViewModel = qrCodeViewModel,
                            firstSettingFunnelsViewModel = firstSettingFunnelsViewModel,
                            checkCameraPermission = checkCameraPermissionFirstSetting,
                            launchGoogleLocationAndAddress = launchGoogleLocationAndAddress,
                            context = context
                        )
                        return@BackgroundImage
                    }

                    if (authUiState.accessToken == null) {
                        LoginScreen(
                            onLoginWithKakao = ::handleLoginWithKakao
                        )
                        return@BackgroundImage
                    }

                    if (authUiState.user != null) {
                        Router(
                            navController = navController,
                            authViewModel = authViewModel,
                            isActiveSleepForegroundService = isActiveSleepForegroundService,
                            healthConnectManager = healthConnectManager,
                            onStartSleepForegroundService = onStartSleepForegroundService,
                            onStopSleepForegroundService = onStopSleepForegroundService,
                            myHouseViewModel = myHouseViewModel,
                            myHouseDetailViewModel = myHouseDetailViewModel,
                            qrCodeViewModel = qrCodeViewModel,
                            checkCameraPermissionHubToHouse = checkCameraPermissionHubToHouse,
                            checkCameraPermissionMachineToHub = checkCameraPermissionMachineToHub,
                            launchGoogleLocationAndAddress = launchGoogleLocationAndAddress
                        )
                    }
                }
            }
        }
    }
}
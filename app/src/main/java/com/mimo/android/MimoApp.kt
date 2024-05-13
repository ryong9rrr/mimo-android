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
import com.mimo.android.apis.mimo.users.postAccessToken
import com.mimo.android.components.BackgroundImage
import com.mimo.android.services.health.HealthConnectManager
import com.mimo.android.screens.*
import com.mimo.android.screens.firstsettingfunnels.*
import com.mimo.android.screens.login.LoginScreen
import com.mimo.android.screens.main.myhome.Home
import com.mimo.android.screens.main.myhome.MyHomeViewModel
import com.mimo.android.services.kakao.loginWithKakao

private const val TAG = "MimoApp"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MimoApp(
    context: Context,
    isActiveSleepForegroundService: Boolean,
    authViewModel: AuthViewModel,
    qrCodeViewModel: QrCodeViewModel,
    firstSettingFunnelsViewModel: FirstSettingFunnelsViewModel,
    healthConnectManager: HealthConnectManager,
    launchGoogleLocationAndAddress: (cb: (userLocation: UserLocation?) -> Unit) -> Unit,
    onStartSleepForegroundService: (() -> Unit)? = null,
    onStopSleepForegroundService: (() -> Unit)? = null,
    checkCameraPermissionFirstSetting: () -> Unit,
    checkCameraPermissionHubToHouse: () -> Unit,
    checkCameraPermissionMachineToHub: () -> Unit,
    myHomeViewModel: MyHomeViewModel
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
                Navigation(navController = navController)
                return@Scaffold
                if (authUiState.user != null && firstSettingFunnelsUiState.currentStepId == null) {
                    Navigation(navController = navController)
                }
            }
        ) {
            BackgroundImage {
                Box(modifier = Modifier.padding(16.dp)) {
                    val currentHome = Home(
                        homeId = 1,
                        items = arrayOf("조명", "무드등"),
                        homeName = "상윤이의 자취방",
                        address = "서울특별시 관악구 봉천동 1234-56"
                    )
                    val anotherHomeList: List<Home> = mutableListOf(
                        Home(
                            homeId = 2,
                            items = arrayOf("조명", "창문", "커튼"),
                            homeName = "상윤이의 본가",
                            address = "경기도 고양시 일산서구 산현로12 경기도 고양시 일산서구 산현로12 경기도 고양시 일산서구 산현로12 경기도 고양시 일산서구 산현로12 경기도 고양시 일산서구 산현로12"
                        ),
                        Home(
                            homeId = 3,
                            items = arrayOf("조명", "커튼"),
                            homeName = "낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가",
                            address = "서울특별시 강남구 테헤란로 212"
                        ),
                        Home(
                            homeId = 4,
                            items = arrayOf("조명", "커튼"),
                            homeName = "싸피",
                            address = "서울특별시 강남구 테헤란로 212"
                        ),
                        Home(
                            homeId = 5,
                            items = arrayOf("조명", "커튼"),
                            homeName = "싸피",
                            address = "서울특별시 강남구 테헤란로 212"
                        )
                    )

                    myHomeViewModel.updateCurrentHome(currentHome)
                    myHomeViewModel.updateAnotherHomeList(anotherHomeList)

                    Router(
                        navController = navController,
                        isActiveSleepForegroundService = isActiveSleepForegroundService,
                        healthConnectManager = healthConnectManager,
                        onStartSleepForegroundService = onStartSleepForegroundService,
                        onStopSleepForegroundService = onStopSleepForegroundService,
                        myHomeViewModel = myHomeViewModel,
                        qrCodeViewModel = qrCodeViewModel,
                        checkCameraPermissionHubToHouse = checkCameraPermissionHubToHouse,
                        checkCameraPermissionMachineToHub = checkCameraPermissionMachineToHub,
                        authViewModel = authViewModel
                    )

                    return@BackgroundImage

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
                            isActiveSleepForegroundService = isActiveSleepForegroundService,
                            healthConnectManager = healthConnectManager,
                            onStartSleepForegroundService = onStartSleepForegroundService,
                            onStopSleepForegroundService = onStopSleepForegroundService,
                            myHomeViewModel = myHomeViewModel,
                            qrCodeViewModel = qrCodeViewModel,
                            checkCameraPermissionHubToHouse = checkCameraPermissionHubToHouse,
                            checkCameraPermissionMachineToHub = checkCameraPermissionMachineToHub,
                            authViewModel = authViewModel
                        )
                    }
                }
            }
        }
    }
}
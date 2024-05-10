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
import com.mimo.android.apis.mimo.user.postAccessToken
import com.mimo.android.components.BackgroundImage
import com.mimo.android.services.health.HealthConnectManager
import com.mimo.android.screens.*
import com.mimo.android.screens.firstsettingfunnels.*
import com.mimo.android.screens.login.LoginScreen
import com.mimo.android.services.kakao.loginWithKakao
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.getData

const val TAG = "MimoApp"

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

        authViewModel.init(
            firstSettingFunnelsViewModel = firstSettingFunnelsViewModel
        )

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
                            checkCameraPermission = checkCameraPermission,
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
                            healthConnectManager = healthConnectManager,
//                serviceRunning = serviceRunning,
//                currentLocation = currentLocation,
//                onClickForeground = onClickForeground,
                        )
                    }
                }
            }
        }
    }
}
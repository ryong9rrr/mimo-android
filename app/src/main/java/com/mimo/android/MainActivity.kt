package com.mimo.android

import android.content.Context
import com.journeyapps.barcodescanner.ScanContract
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import com.mimo.android.apis.mimo.createMimoApiService
import com.mimo.android.services.health.*
import com.mimo.android.services.gogglelocation.*
import com.mimo.android.services.kakao.initializeKakaoSdk
import com.mimo.android.services.kakao.logoutWithKakao
import com.mimo.android.services.qrcode.*
import com.mimo.android.utils.backpresshandler.initializeWhenTwiceBackPressExitApp
import com.mimo.android.utils.os.printKeyHash
import com.mimo.android.utils.preferences.createSharedPreferences

class MainActivity : ComponentActivity() {
    // health-connect
    private lateinit var healthConnectManager: HealthConnectManager
    private lateinit var healthConnectPermissionRequest: ActivityResultLauncher<Set<String>>

    private val authViewModel = AuthViewModel()
    private val firstSettingFunnelsViewModel = FirstSettingFunnelsViewModel()
    private val qrCodeViewModel = QrCodeViewModel()

    // QR code Scanner
    private val barCodeLauncher = registerForActivityResult(ScanContract()) {
            result ->
        if (result.contents == null) {
            qrCodeViewModel.removeQrCode()
            Toast.makeText(
                this@MainActivity,
                "취소",
                Toast.LENGTH_SHORT
            ).show()
            return@registerForActivityResult
        }
        Toast.makeText(
            this@MainActivity,
            result.contents,
            Toast.LENGTH_SHORT
        ).show()

        qrCodeViewModel.updateQrCode(result.contents)
        firstSettingFunnelsViewModel.updateCurrentStep(stepId = R.string.first_setting_funnel_hub_find_waiting)
    }

    private val qRRequestPermissionLauncher = createQRRequestPermissionLauncher(
        barCodeLauncher = barCodeLauncher
    )

    init {
        instance = this
    }

    companion object {
        lateinit var instance: MainActivity
        fun getMainActivityContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        printKeyHash(this)
        initializeWhenTwiceBackPressExitApp(this) // 안드로이드OS 뒤로가기 연속 2번 누르면 앱을 종료시키는 핸들러 추가
        initializeKakaoSdk(this) // kakao sdk 초기화
        createSharedPreferences() // 스토리지 초기화
        createMimoApiService() // mimo api 초기화

        // health-connect 권한 요청
        healthConnectManager = (application as BaseApplication).healthConnectManager
        if (checkAvailability()) {
            healthConnectPermissionRequest = createHealthConnectPermissionRequest(
                healthConnectManager = healthConnectManager,
                context = this
            )
            checkHealthConnectPermission(
                showInfo = false,
                healthConnectManager = healthConnectManager,
                context = this,
                healthConnectPermissionRequest = healthConnectPermissionRequest
            )
        }

        // 위치 권한 요청
        RequestPermissionsUtil(this).requestLocation()
    }

    override fun onStart() {
        super.onStart()

        setContent {
            MimoApp(
                authViewModel = authViewModel,
                healthConnectManager = healthConnectManager,
                qrCodeViewModel = qrCodeViewModel,
                checkCameraPermission = { checkCameraPermission(
                    context = this,
                    barCodeLauncher = barCodeLauncher,
                    qRRequestPermissionLauncher = qRRequestPermissionLauncher
                ) },
                firstSettingFunnelsViewModel = firstSettingFunnelsViewModel,
                launchGoogleLocationAndAddress = { cb: (userLocation: UserLocation?) -> Unit -> launchGoogleLocationAndAddress(cb = cb) },
//                serviceRunning = serviceBoundState,
//                currentLocation = displayableLocation,
//                onClickForeground = ::onStartOrStopForegroundServiceClick,
                context = this,
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // TODO: 개발 중에는 그냥 매번 로그아웃 처리
        authViewModel.logout()
        logoutWithKakao()
    }
}
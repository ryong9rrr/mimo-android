package com.mimo.android

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import com.journeyapps.barcodescanner.ScanContract
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.mimo.android.apis.mimo.createMimoApiService
import com.mimo.android.services.health.*
import com.mimo.android.services.gogglelocation.*
import com.mimo.android.services.kakao.initializeKakaoSdk
import com.mimo.android.services.qrcode.*
import com.mimo.android.utils.backpresshandler.initializeWhenTwiceBackPressExitApp
import com.mimo.android.utils.os.printKeyHash
import com.mimo.android.utils.preferences.createSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private const val TAG = "MainActivity"

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

        // 포그라운드 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

        authViewModel.checkAlreadyLoggedIn(
            firstSettingFunnelsViewModel = firstSettingFunnelsViewModel
        )

        setContent {
            MimoApp(
                context = this,
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
                onStartSleepForegroundService = ::handleStartSleepForegroundService,
                onStopSleepForegroundService = ::handleStopSleepForegroundService
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.i(TAG, "App destroy")
    }


    private val scope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null
    private fun handleStartSleepForegroundService(){
        Intent(getMainActivityContext(), SleepForegroundService::class.java).also {
            it.action = SleepForegroundService.Actions.START.toString()
            startService(it)

            job = createJob()
        }
    }
    private fun handleStopSleepForegroundService(){
        Intent(applicationContext, SleepForegroundService::class.java).also {
            it.action = SleepForegroundService.Actions.STOP.toString()
            startService(it)
            job?.cancel()
        }
    }

    private fun createJob(): Job{
        return scope.launch {
            while (true) {
                val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
                val now = Instant.now()
                val step = healthConnectManager.readSteps(startOfDay.toInstant(), now)
                Log.d(TAG, "MIMO가 감지 중 @@ ${getCurrentTime()} @@ ${step}")
                delay(15 * 60 * 1000 + 30 * 1000) // 15분 30초
            }
        }
    }

    private fun getCurrentTime(): String{
        val zoneId = ZoneId.of("Asia/Seoul") // 한국 시간대 (KST)
        val currentTimeKST = ZonedDateTime.now(zoneId) // 현재 한국 시간

        // 월, 일, 시, 분, 초 추출
        val month = currentTimeKST.monthValue
        val day = currentTimeKST.dayOfMonth
        val hour = currentTimeKST.hour
        val minute = currentTimeKST.minute
        val second = currentTimeKST.second

        // 형식 지정
        val formatter = DateTimeFormatter.ofPattern("M월 d일 H시 m분 s초")

        // 포맷에 따라 날짜 및 시간을 문자열로 변환하여 반환
        return currentTimeKST.format(formatter)
    }
}
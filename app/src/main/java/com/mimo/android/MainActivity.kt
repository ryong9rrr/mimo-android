package com.mimo.android

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import com.journeyapps.barcodescanner.ScanContract
import android.os.Bundle
import android.util.Log
import android.util.TimeUtils
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Timer
import java.util.TimerTask
import kotlin.reflect.typeOf

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

    // private var job: Job? = null
    private var timerTask: TimerTask? = null

    private fun handleStartSleepForegroundService(){
        Intent(getMainActivityContext(), SleepForegroundService::class.java).also {
            it.action = SleepForegroundService.Actions.START.toString()
            startService(it)

            //job = createJob()

            timerTask = Task()
            Timer().scheduleAtFixedRate(timerTask, 1000, FIFTEEN_MINUTES)
        }
    }
    private fun handleStopSleepForegroundService(){
        Intent(applicationContext, SleepForegroundService::class.java).also {
            it.action = SleepForegroundService.Actions.STOP.toString()
            startService(it)
            //job?.cancel()
            timerTask?.cancel()
            timerTask = null
        }
    }

//    private fun createJob(): Job{
//        return scope.launch {
//            while (true) {
//                //readLastSleepStage()
//                //readSleepSession(4, 28) // 4월 28일의 기록
//                readSteps()
//                delay(15 * 60 * 1000L) // 15분
//            }
//        }
//    }

    inner class Task: TimerTask() {
        override fun run() {
            lifecycleScope.launch {
                readLastSleepStage()
            }
        }

        override fun cancel(): Boolean {
            return super.cancel()
        }
    }

    private suspend fun readSleepSession(
        month: Int,
        dayOfMonth: Int,
    ){
        val startTime = ZonedDateTime.of(2024, month, dayOfMonth, 0, 0, 0, 0, ZoneId.of("Asia/Seoul"))
        val endTime = ZonedDateTime.of(2024, month, dayOfMonth, 23, 59, 59, 0, ZoneId.of("Asia/Seoul"))

        val sleepSessionRecord = healthConnectManager.readSleepSessionRecord(startTime.toInstant(), endTime.toInstant())

        if (sleepSessionRecord == null) {
            Log.d(TAG, "MIMO가 감지 중")
            Log.d(TAG, "${dateFormatter.format(startTime)} ~ ${dateFormatter.format(endTime)} 까지 수면기록 없음")
            return
        }
        sleepSessionRecord.forEachIndexed() { sessionIndex, session ->
            val koreanStartTime = dateFormatter.format(session.startTime)
            val koreanEndTime = dateFormatter.format(session.endTime)
            Log.d(TAG, "@@@@@@@ 상세 수면 기록 @@@@@@@")
            Log.d(TAG, "수면 ${sessionIndex + 1} 전체 : $koreanStartTime ~ $koreanEndTime")
            session.stages.forEach() { stage ->
                Log.d(TAG, "${dateFormatter.format(stage.startTime)} ~ ${dateFormatter.format(stage.endTime)} @@ ${meanStage(stage.stage)}")
            }
        }
    }

    private suspend fun readLastSleepStage(){
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val now = Instant.now()
        val lastSleepStage = healthConnectManager.readLastSleepStage(startOfDay.toInstant(), now)
        if (lastSleepStage == null) {
            Log.d(TAG, "MIMO가 감지 중 @@ ${getCurrentTime()} @@ 수면기록이 감지되지 않음")
            return
        }
        Log.d(TAG, "MIMO가 감지 중 @@ ${getCurrentTime()} @@ ${dateFormatter.format(lastSleepStage.startTime)} ~ ${dateFormatter.format(lastSleepStage.endTime)} @@ ${meanStage(lastSleepStage.stage)}")
    }

    private suspend fun readSteps(){
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val now = Instant.now()
        val step = healthConnectManager.readSteps(startOfDay.toInstant(), now)
        Log.d(TAG, "MIMO가 감지 중 @@ ${getCurrentTime()} @@ ${step}")
    }

    val dateFormatter = DateTimeFormatter.ofPattern("HH:mm")
        .withZone(ZoneId.of("Asia/Seoul"))

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

const val FIFTEEN_MINUTES = 15 * 60 * 1000L

enum class SleepStage {
    UNKNOWN, // 0
    AWAKE, // 1
    SLEEPING, // 2
    OUT_OF_BED, // 3
    LIGHT, // 4
    DEEP, // 5
    REM, // 6
    AWAKE_IN_BED // 7
}

// TODO: 수면기록은 AWAKE(수면 중 깸), LIGHT(얕은 잠), DEEP(깊은 잠), REM(렘 수면) 이렇게 4가지만 찍히는 걸로 확인됨..
// TODO: 따라서 사실 상 이 기록이 찍히게 되면 수면이 시작됐다는 거고 AWAKE가 찍히면 수면 중 깼다는 것...
// TODO: 그니까 이 기록이 찍히면 불 꺼주면 된다. 어차피 깨우는 건 30분 전부터 서서히 켜주면 되니까... 기상 감지는 못해도 괜찮을지도...
fun meanStage(stage: Int): SleepStage {
    if (stage == 1) {
        return SleepStage.AWAKE
    }
    if (stage == 2) {
        return SleepStage.SLEEPING
    }
    if (stage == 3) {
        return SleepStage.OUT_OF_BED
    }
    if (stage == 4) {
        return SleepStage.LIGHT
    }
    if (stage == 5) {
        return SleepStage.DEEP
    }
    if (stage == 6) {
        return SleepStage.REM
    }
    if (stage == 7) {
        return SleepStage.AWAKE_IN_BED
    }
    return SleepStage.UNKNOWN
}
package com.mimo.android

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

//        // check location permission
//        checkAndRequestNotificationPermission()
//        tryToBindToServiceIfRunning()

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

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()

        // TODO: 개발 중에는 그냥 매번 로그아웃 처리
        authViewModel.logout()
        logoutWithKakao()
    }

//    // location-foreground sample
//    private var exampleService: ExampleLocationForegroundService? = null
//    private var serviceBoundState by mutableStateOf(false)
//    private var displayableLocation by mutableStateOf<String?>(null)
//
//    // needed to communicate with the service.
//    private val connection = object : ServiceConnection {
//
//        override fun onServiceConnected(className: ComponentName, service: IBinder) {
//            // we've bound to ExampleLocationForegroundService, cast the IBinder and get ExampleLocationForegroundService instance.
//            Log.d(TAG, "onServiceConnected")
//
//            val binder = service as ExampleLocationForegroundService.LocalBinder
//            exampleService = binder.getService()
//            serviceBoundState = true
//
//            onServiceConnected()
//        }
//
//        override fun onServiceDisconnected(arg0: ComponentName) {
//            // This is called when the connection with the service has been disconnected. Clean up.
//            Log.d(TAG, "onServiceDisconnected")
//
//            serviceBoundState = false
//            exampleService = null
//        }
//    }
//
//    // we need notification permission to be able to display a notification for the foreground service
//    private val notificationPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) {
//            // if permission was denied, the service can still run only the notification won't be visible
//        }
//
//    // we need location permission to be able to start the service
//    private val locationPermissionRequest = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        when {
//            permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
//                // Precise location access granted, service can run
//                startForegroundService()
//            }
//
//            permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
//                // Only approximate location access granted, service can still run.
//                startForegroundService()
//            }
//
//            else -> {
//                // No location access granted, service can't be started as it will crash
//                Toast.makeText(this, "Location permission is required!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        unbindService(connection)
//    }
//
//    /**
//     * Check for notification permission before starting the service so that the notification is visible
//     */
//    private fun checkAndRequestNotificationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            when (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)) {
//                android.content.pm.PackageManager.PERMISSION_GRANTED -> {
//                    // permission already granted
//                }
//
//                else -> {
//                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
//                }
//            }
//        }
//    }
//
//    private fun onStartOrStopForegroundServiceClick() {
//        if (exampleService == null) {
//            // service is not yet running, start it after permission check
//            locationPermissionRequest.launch(
//                arrayOf(
//                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//        } else {
//            // service is already running, stop it
//            exampleService?.stopForegroundService()
//        }
//    }
//
//    /**
//     * Creates and starts the ExampleLocationForegroundService as a foreground service.
//     *
//     * It also tries to bind to the service to update the UI with location updates.
//     */
//    private fun startForegroundService() {
//        // start the service
//        startForegroundService(Intent(this, ExampleLocationForegroundService::class.java))
//
//        // bind to the service to update UI
//        tryToBindToServiceIfRunning()
//    }
//
//    private fun tryToBindToServiceIfRunning() {
//        Intent(this, ExampleLocationForegroundService::class.java).also { intent ->
//            bindService(intent, connection, 0)
//        }
//    }
//
//    private fun onServiceConnected() {
//        lifecycleScope.launch {
//            // observe location updates from the service
//            exampleService?.locationFlow?.map {
//                it?.let { location ->
//                    "${location.latitude}, ${location.longitude}"
//                }
//            }?.collectLatest {
//                displayableLocation = it
//            }
//        }
//    }
}
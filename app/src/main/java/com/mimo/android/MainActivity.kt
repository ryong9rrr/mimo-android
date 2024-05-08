package com.mimo.android

import com.journeyapps.barcodescanner.ScanContract
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import com.mimo.android.services.health.*
import com.mimo.android.services.gogglelocation.*
import com.mimo.android.services.qrcode.*

class MainActivity : ComponentActivity() {
    // 뒤로가기 2번 눌러 앱 종료하기
    // jetpack compose의 BackHandler의 우선순위가 더 높기 때문에 앱종료시키고 싶지 않다면 BackHandler를 정의하면 됨
    // 네비게이션으로 이동한 상태여도 네비게이션 뒤로가기 우선순위가 더 높음
    private var backKeyPressedTime = 0L
    private val onBackPressedCallback: OnBackPressedCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis()
                Toast.makeText(this@MainActivity, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
    }


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

    override fun onStart() {
        super.onStart()
        RequestPermissionsUtil(this).requestLocation() // 위치 권한 요청
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        // health-connect instance 생성
        healthConnectManager = (application as BaseApplication).healthConnectManager
        // health-connect 권한 요청
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

//        // check location permission
//        checkAndRequestNotificationPermission()
//        tryToBindToServiceIfRunning()

        setContent {
            // TODO: 이미 로그인이 되어있는지 아닌지 확인 (처음부터 로그인이 되어있는 경우를 체크)
            authViewModel.init(user = null)

            // TODO: 로그인이 되어있는 상태가 아니라면 초기설정퍼널을 시작할것인지 아닌지 결정(집이나 허브가 없다면 시작)

            // check firstSetting
            // TODO: 로그인이 됐는지 확인하고 로그인이 된 상태이며 집이나 허브가 없다면 아래 init() 실행
            if (true) {
//                firstSettingFunnelsViewModel.init(
//                    currentStepId = R.string.first_setting_funnel_first_setting_start
//                )
//                firstSettingFunnelsViewModel.init(
//                    currentStepId = R.string.test_funnel
//                )
            }

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
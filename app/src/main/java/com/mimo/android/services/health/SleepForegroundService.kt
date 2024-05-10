package com.mimo.android.services.health

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ServiceCompat
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.mimo.android.examples.ExampleLocationForegroundService
import com.mimo.android.examples.NotificationsHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class SleepForegroundService: Service() {
    private val binder = LocalBinder()
    private val coroutineScope = CoroutineScope(Job())
    private var timerJob: Job? = null

    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(this) }

    // TODO: 임시로 Any
    private val _sleepFlow = MutableStateFlow<Any?>(null)
    var sleepFlow: StateFlow<Any?> = _sleepFlow

    private val _locationFlow = MutableStateFlow<Location?>(null)
    var locationFlow: StateFlow<Location?> = _locationFlow

    override fun onCreate() {
        super.onCreate()
        Log.d(SleepForegroundService.TAG, "onCreate")

        Toast.makeText(this, "Foreground Service created", Toast.LENGTH_SHORT).show()

        startServiceRunningTicker()
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(SleepForegroundService.TAG, "onBind")
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(SleepForegroundService.TAG, "onStartCommand")

        startAsForegroundService()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startAsForegroundService() {
        // create the notification channel
        NotificationsHelper.createNotificationChannel(this)

        // promote service to foreground service
        ServiceCompat.startForeground(
            this,
            1,
            NotificationsHelper.buildNotification(this),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            } else {
                0
            }
        )
    }

    inner class LocalBinder : Binder() {
        fun getService(): SleepForegroundService = this@SleepForegroundService
    }

    private fun startServiceRunningTicker() {
        timerJob?.cancel()
        timerJob = coroutineScope.launch {
            tickerFlow()
                .collectLatest {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@SleepForegroundService,
                            "Foreground Service still running!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun tickerFlow(
        period: Duration = SleepForegroundService.TICKER_PERIOD_MINUTES,
        initialDelay: Duration = SleepForegroundService.TICKER_PERIOD_MINUTES
    ) = flow {
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    suspend fun readLastSleepSession(start: Instant, end: Instant): List<SleepSessionRecord.Stage>?{
        val request = ReadRecordsRequest(
            recordType = SleepSessionRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request)
        if (response.records.isNotEmpty()) {
            val sleepSessionRecord = response.records.last()
            return sleepSessionRecord.stages
        }
        return null
    }

    companion object {
        private const val TAG = "services/health/SleepForegroundService"
        private val TICKER_PERIOD_MINUTES = 1.minutes
    }
}
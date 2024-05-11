package com.mimo.android.services.health

import com.mimo.android.R
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SleepForegroundService: Service() {

    private val _stepFlow = MutableStateFlow<String?>(null)
    var stepFlow: StateFlow<String?> = _stepFlow

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){
        val notification = NotificationCompat
            .Builder(this, "running_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Mimo")
            .setContentText("수면 감지 중")
            .build()

        startForeground(1, notification)
    }

    enum class Actions {
        START, STOP
    }
}
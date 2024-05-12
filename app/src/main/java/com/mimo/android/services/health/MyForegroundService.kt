package com.mimo.android.services.health

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.*
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import com.mimo.android.R

//class MyForegroundService: Service() {
//    companion object {
//        private const val CHANNEL_ID = "MyForegroundServiceChannel"
//        private const val NOTIFICATION_ID = 101
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        createNotificationChannel()
//        val notification = createNotification()
//        startForeground(NOTIFICATION_ID, notification)
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val workRequest = PeriodicWorkRequestBuilder<MyWorker>(
//            repeatInterval = 15, // 15분마다
//            repeatIntervalTimeUnit = TimeUnit.MINUTES
//        ).build()
//
//        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
//            "MyUniqueWorkName",
//            ExistingPeriodicWorkPolicy.REPLACE,
//            workRequest
//        )
//
//        return START_STICKY
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                "Foreground Service Channel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val manager = getSystemService(NotificationManager::class.java)
//            manager.createNotificationChannel(channel)
//        }
//    }
//
//    private fun createNotification(): Notification {
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("Foreground Service")
//            .setContentText("15분마다 API 요청을 수행 중입니다.")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//        return builder.build()
//    }
//}
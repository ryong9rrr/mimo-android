package com.mimo.android.services.health

import android.app.Notification
import android.os.Handler
import android.os.Looper
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.mimo.android.apis.sleeps.PostSleepDataRequest
import com.mimo.android.apis.sleeps.postSleepData
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.getData
import java.time.LocalDateTime
import java.time.ZoneId

private const val TAG = "SleepNotificationListenerService"

class SleepNotificationListenerService: NotificationListenerService() {

    // Firebase Realtime Database의 "messages" 노드에 참조를 가져옴
    private val database = Firebase.database("https://mimo-14710-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val ref = database.getReference(getData(ACCESS_TOKEN) ?: "messages")

    private val handlerNotificationPosted = Handler(Looper.getMainLooper())
    private val handlerNotificationRemoved = Handler(Looper.getMainLooper())
    private var runnableNotificationPosted: Runnable? = null
    private var runnableNotificationRemoved: Runnable? = null
    private val debounceDelay: Long = 5000 // 5 seconds

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        debounceNotificationPosted(sbn)
    }
    private fun debounceNotificationPosted(sbn: StatusBarNotification?) {
        // 5초전에 걸려있는 removed가 있다면 호출되지 않도록 제거
        runnableNotificationRemoved?.let { handlerNotificationRemoved.removeCallbacks(it) }

        // Cancel the previous runnable if it exists
        runnableNotificationPosted?.let { handlerNotificationPosted.removeCallbacks(it) }

        // Create a new runnable
        runnableNotificationPosted = Runnable {
            sbn?.let {
                handleDebouncedNotificationPosted(it)
            }
        }

        // Post the new runnable with a delay
        handlerNotificationPosted.postDelayed(runnableNotificationPosted!!, debounceDelay)
    }
    private fun handleDebouncedNotificationPosted(sbn: StatusBarNotification) {
        val packageName: String = sbn.packageName ?: "Null"
        val extras = sbn.notification?.extras
        val extraTitle: String = extras?.get(Notification.EXTRA_TITLE).toString()
        val extraText: String = extras?.get(Notification.EXTRA_TEXT).toString()
        Log.i(TAG, "알림 리스너 서비스 onNotificationPosted : ${packageName} / ${extraTitle} / ${extraText}")
        if (packageName == "com.northcube.sleepcycle") {
            postSleepData(
                accessToken = getData(ACCESS_TOKEN) ?: "",
                postSleepDataRequest = PostSleepDataRequest(
                    sleepLevel = 4
                )
            )
            ref.push().setValue("${getCurrentKoreaTime()}에 슬립사이클 앱에서 onNotificationPosted (수면시작)")
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        debounceNotificationRemoved(sbn)
    }
    private fun debounceNotificationRemoved(sbn: StatusBarNotification?) {
        // 슬립사이클 포그라운드가 종료될 때 notificationPosted가 먼저 한번 호출되므로 제거
        runnableNotificationPosted?.let { handlerNotificationPosted.removeCallbacks(it) }

        // Cancel the previous runnable if it exists
        runnableNotificationRemoved?.let { handlerNotificationRemoved.removeCallbacks(it) }

        // Create a new runnable
        runnableNotificationRemoved = Runnable {
            sbn?.let {
                handleDebouncedNotificationRemoved(it)
            }
        }

        // Post the new runnable with a delay
        handlerNotificationRemoved.postDelayed(runnableNotificationRemoved!!, debounceDelay)
    }
    private fun handleDebouncedNotificationRemoved(sbn: StatusBarNotification) {
        val packageName: String = sbn.packageName ?: "Null"
        val extras = sbn.notification?.extras
        val extraTitle: String = extras?.get(Notification.EXTRA_TITLE).toString()
        val extraText: String = extras?.get(Notification.EXTRA_TEXT).toString()
        Log.i(TAG, "알림 리스너 서비스 onNotificationRemoved : ${packageName} / ${extraTitle} / ${extraText}")
        if (packageName == "com.northcube.sleepcycle") {
            postSleepData(
                accessToken = getData(ACCESS_TOKEN) ?: "",
                postSleepDataRequest = PostSleepDataRequest(
                    sleepLevel = -1
                )
            )
            ref.push().setValue("${getCurrentKoreaTime()}에 슬립사이클 앱에서 onNotificationRemoved(수면종료)")
        }
    }
}

private fun getCurrentKoreaTime(): String{
    val zoneId = ZoneId.of("Asia/Seoul")

    // 현재 한국 시간 가져오기
    val currentDateTime = LocalDateTime.now(zoneId)

    // 월, 일, 시, 분, 초 가져오기
    val month = currentDateTime.monthValue
    val day = currentDateTime.dayOfMonth
    val hour = currentDateTime.hour
    val minute = currentDateTime.minute
    val second = currentDateTime.second

    return "${month}월 ${day}일 ${hour}:${minute}:${second}"
}
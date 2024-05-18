package com.mimo.android.services.health

import android.app.Notification
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
    val database = Firebase.database("https://mimo-14710-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference(getData(ACCESS_TOKEN) ?: "messages")

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        Log.i(TAG, "알림 리스너 서비스 onNotificationPosted")

        val packageName: String = sbn?.packageName ?: "Null"
        val extras = sbn?.notification?.extras
        val extraTitle: String = extras?.get(Notification.EXTRA_TITLE).toString()
        val extraText: String = extras?.get(Notification.EXTRA_TEXT).toString()

        if (packageName == "com.northcube.sleepcycle") {
            Log.i(TAG, packageName)
            Log.i(TAG, extraTitle)
            Log.i(TAG, extraText)
            postSleepData(
                accessToken = getData(ACCESS_TOKEN) ?: "",
                postSleepDataRequest = PostSleepDataRequest(
                    sleepLevel = 4
                )
            )
            ref.push().setValue("${getCurrentKoreaTime()}에 슬립사이클 앱 켜짐")
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
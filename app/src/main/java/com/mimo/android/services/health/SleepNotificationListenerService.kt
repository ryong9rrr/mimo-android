package com.mimo.android.services.health

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.mimo.android.apis.sleeps.PostSleepDataRequest
import com.mimo.android.apis.sleeps.postSleepData
import com.mimo.android.utils.preferences.ACCESS_TOKEN
import com.mimo.android.utils.preferences.getData
import com.mimo.android.utils.showToast

private const val TAG = "SleepNotificationListenerService"

class SleepNotificationListenerService: NotificationListenerService() {
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
        }
    }
}
package com.mimo.android.screens.main.sleep

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mimo.android.components.*
import com.mimo.android.components.base.Size
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

private const val TAG = "SleepScreen"

@Composable
fun SleepScreen(
    navController: NavHostController,
    isActiveSleepForegroundService: Boolean,
    onStartSleepForegroundService: (() -> Unit)? = null,
    onStopSleepForegroundService: (() -> Unit)? = null
){
    Column {
        HeadingLarge(text = "수면과 기상", fontSize = Size.lg)
        Spacer(modifier = Modifier.padding(14.dp))

        if (isActiveSleepForegroundService) {
            Button(text = "수면 끝", onClick = onStopSleepForegroundService)
        } else {
            Button(text = "수면 시작", onClick = onStartSleepForegroundService)
        }
    }
}

private fun getCurrentTime(): ZonedDateTime {
    val zoneId = ZoneId.of("Asia/Seoul")
    val currentTimeUtc = LocalDateTime.now() // 현재 시간을 UTC 시간으로 가져오기
    return ZonedDateTime.of(currentTimeUtc, zoneId) // UTC 시간을 한국 시간대로 변환
}
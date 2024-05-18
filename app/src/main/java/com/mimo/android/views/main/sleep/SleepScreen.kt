package com.mimo.android.views.main.sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mimo.android.components.*
import com.mimo.android.components.base.Size
import com.mimo.android.ui.theme.Teal100
import com.mimo.android.ui.theme.Teal400
import com.mimo.android.ui.theme.Teal900
import com.mimo.android.utils.showToast
import com.mimo.android.viewmodels.MyHouseViewModel
import com.mimo.android.viewmodels.SleepViewModel

private const val TAG = "SleepScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepScreen(
    navController: NavHostController? = null,
    isActiveSleepForegroundService: Boolean,
    onStartSleepForegroundService: (() -> Unit)? = null,
    onStopSleepForegroundService: (() -> Unit)? = null,
    myHouseViewModel: MyHouseViewModel,
    sleepViewModel: SleepViewModel
){
    val state = rememberTimePickerState()
    val sleepUiState by sleepViewModel.uiState.collectAsState()
    val currentHouseNickname = myHouseViewModel.getCurrentHouse()?.nickname

    var selectedHour by remember { mutableStateOf(7) }
    var selectedMinute by remember { mutableStateOf(30) }

    LaunchedEffect(Unit) {
        sleepViewModel.fetchGetWakeupTime()
    }

    Column {
        HeadingLarge(text = "수면과 기상", fontSize = Size.lg)
        Spacer(modifier = Modifier.padding(14.dp))

        if (currentHouseNickname != null) {
            HorizontalScroll {
                HeadingSmall(text = "$currentHouseNickname", color = Teal100)
                HeadingSmall(text = "에서")
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        HeadingSmall(text = "기상 30 전 부터 깨워드릴게요")

        Spacer(modifier = Modifier.weight(1f))
        TimePicker(
            hour = selectedHour,
            minute = selectedMinute,
            onChangeValue = { time: TimePickerTime ->
                selectedHour = time.hour
                selectedMinute = time.minute
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "${makeTextTimeMinus30Minutes(selectedHour, selectedMinute)}부터 천천히 깨워드려요",
                color = Teal100
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            if (isActiveSleepForegroundService) {
                Button(text = "수면 끝", onClick = onStopSleepForegroundService, width = 300.dp)
            } else {
                Button(
                    text = "수면 시작",
                    onClick = {
                        showToast("${makeTextTimeMinus30Minutes(selectedHour, selectedMinute)}부터 천천히 깨워드릴게요")

                        onStartSleepForegroundService?.invoke()
                    },
                    width = 300.dp
                )
            }
        }
        Spacer(modifier = Modifier.weight(2f))
        Spacer(modifier = Modifier.padding(44.dp))
    }
}

private fun makeTextTimeMinus30Minutes(
    hour: Int,
    minute: Int
): String {
    // 30분 전의 시간을 계산
    var newHour = hour
    var newMinute = minute - 30

    if (newMinute < 0) {
        newMinute += 60
        newHour -= 1
        if (newHour < 0) {
            newHour = 23
        }
    }

    // 시간과 분을 두 자리 문자열로 변환
    val newHourString = newHour.toString().padStart(2, '0')
    val newMinuteString = newMinute.toString().padStart(2, '0')

    // 새로운 시간을 hh시mm분 형식의 문자열로 반환
    return "${newHourString}시 ${newMinuteString}분"
}

@Preview
@Composable
private fun SleepScreenPreview(){
    val myHouseViewModel = MyHouseViewModel()
    val sleepViewModel = SleepViewModel()

    SleepScreen(
        isActiveSleepForegroundService = false,
        myHouseViewModel = myHouseViewModel,
        sleepViewModel = sleepViewModel
    )
}
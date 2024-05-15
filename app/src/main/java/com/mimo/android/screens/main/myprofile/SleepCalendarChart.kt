package com.mimo.android.screens.main.myprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mimo.android.components.Icon
import com.mimo.android.components.Text
import com.mimo.android.meanStage
import com.mimo.android.services.health.HealthConnectManager
import com.mimo.android.ui.theme.Teal100
import com.mimo.android.utils.dateFormatter
import com.mimo.android.viewmodels.MyProfileViewModel
import com.mimo.android.viewmodels.convertCalendarDate
import java.time.LocalDate

@Composable
fun SleepCalendarChart(
    myProfileViewModel: MyProfileViewModel,
    healthConnectManager: HealthConnectManager? = null,
){
    val myProfileUiState by myProfileViewModel.uiState.collectAsState()

    fun isToday(): Boolean {
        val stateDate = convertCalendarDate(myProfileUiState.date)
        val todayDate = convertCalendarDate(LocalDate.now())
        return stateDate.month == todayDate.month && stateDate.day == todayDate.day
    }

    fun getGoNextDayButtonColor(): Color {
        if (isToday()) {
            return Color.Gray
        }
        return Color.White
    }

    fun handleClickPrevDate(){
        if (healthConnectManager == null) {
            return
        }
        myProfileViewModel.updateToPrevDate(healthConnectManager)
    }

    fun handleClickNextDate(){
        if (healthConnectManager == null) {
            return
        }
        if (isToday()) {
            return
        }
        myProfileViewModel.updateToNextDate(healthConnectManager)
    }

    fun showDateString(): String {
        val stateDate = convertCalendarDate(myProfileUiState.date)
        val todayDate = convertCalendarDate(LocalDate.now())
        if (stateDate.month == todayDate.month && stateDate.day == todayDate.day) {
            return "오늘 (${todayDate.dayOfWeek})"
        }
        return "${stateDate.year}년 ${stateDate.month}월 ${stateDate.day}일 (${stateDate.dayOfWeek})"
    }

    LaunchedEffect(Unit) {
        if (healthConnectManager == null) {
            return@LaunchedEffect
        }
        myProfileViewModel.init(healthConnectManager)
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                size = 24.dp,
                onClick = ::handleClickPrevDate
            )

            Text(text = showDateString())

            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                size = 24.dp,
                onClick = ::handleClickNextDate,
                color = getGoNextDayButtonColor()
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))

        if (myProfileUiState.sleepSessionRecordList == null) {
            Spacer(modifier = Modifier.padding(4.dp))
            Spacer(modifier = Modifier.padding(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "수면 기록이 없어요", color = Teal100)
            }
        } else {
            myProfileUiState.sleepSessionRecordList!!.forEachIndexed { sessionIndex, session ->
                val koreanStartTime = dateFormatter.format(session.startTime)
                val koreanEndTime = dateFormatter.format(session.endTime)
                Text(text = "@@@@@@@ 상세 수면 기록 @@@@@@@")
                Text(text = "수면 ${sessionIndex + 1} 전체 : $koreanStartTime ~ $koreanEndTime")
                session.stages.forEach { stage ->
                    Text(text = "${dateFormatter.format(stage.startTime)} ~ ${dateFormatter.format(stage.endTime)} @@ ${meanStage(stage.stage)}")
                }
            }
        }
    }
}

@Preview
@Composable
private fun SleepCalendarChartPreview(){
    val myProfileViewModel = MyProfileViewModel()

    SleepCalendarChart(myProfileViewModel = myProfileViewModel)
}
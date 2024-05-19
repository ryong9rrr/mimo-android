package com.mimo.android.views.main.myprofile

import SleepPatternChart
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mimo.android.components.Text
import com.mimo.android.ui.theme.Teal100
import com.mimo.android.utils.dateFormatter
import com.mimo.android.viewmodels.MyProfileViewModel
import com.mimo.android.viewmodels.meanStage

@Composable
fun SleepStatistics(
    myProfileViewModel: MyProfileViewModel
){
    val myProfileUiState by myProfileViewModel.uiState.collectAsState()

    if (myProfileUiState.sleepSessionRecordList == null) {
        Column {
            Spacer(modifier = Modifier.padding(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "수면 기록이 없어요", color = Teal100)
            }
        }
    } else {
        Column {
            Spacer(modifier = Modifier.padding(8.dp))

            SleepPatternChart(startHour = 0f, endHour = 22f)

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
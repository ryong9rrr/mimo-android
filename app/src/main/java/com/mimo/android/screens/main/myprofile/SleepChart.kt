package com.mimo.android.screens.main.myprofile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mimo.android.components.Text
import com.mimo.android.meanStage
import com.mimo.android.utils.dateFormatter
import java.time.Instant

@Composable
fun SleepChart(
    sleepSessionRecordList: List<DummySleepSession>
){
    sleepSessionRecordList.forEachIndexed { sessionIndex, session ->
        val koreanStartTime = dateFormatter.format(session.startTime)
        val koreanEndTime = dateFormatter.format(session.endTime)
        Text(text = "@@@@@@@ 상세 수면 기록 @@@@@@@")
        Text(text = "수면 ${sessionIndex + 1} 전체 : $koreanStartTime ~ $koreanEndTime")
        session.stages.forEach { stage ->
            Text(text = "${dateFormatter.format(stage.startTime)} ~ ${dateFormatter.format(stage.endTime)} @@ ${meanStage(stage.stage)}")
        }
    }
}

@Preview
@Composable
private fun SleepChartPreview(){
    val sleepSessionRecordList = mutableListOf<DummySleepSession>()

    SleepChart(sleepSessionRecordList = sleepSessionRecordList)
}

data class DummyStage(
    val startTime: Instant,
    val endTime: Instant,
    val stage: Int
)

data class DummySleepSession(
    val startTime: Instant,
    val endTime: Instant,
    val stages: List<DummyStage>
)
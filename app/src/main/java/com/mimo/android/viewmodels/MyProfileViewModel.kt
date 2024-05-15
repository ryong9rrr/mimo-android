package com.mimo.android.viewmodels

import android.util.Log
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.android.meanStage
import com.mimo.android.services.health.HealthConnectManager
import com.mimo.android.utils.dateFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale
import java.time.format.DateTimeFormatter

private const val TAG = "MyProfileViewModel"

class MyProfileViewModel: ViewModel() {
    private var isInit = false
    private val _uiState = MutableStateFlow(MyProfileUiState(date = LocalDate.now()))
    val uiState: StateFlow<MyProfileUiState> = _uiState.asStateFlow()

    fun init(healthConnectManager: HealthConnectManager){
        if (isInit) {
            return
        }

        viewModelScope.launch {
            fetchSleepSessionRecord(
                date = LocalDate.now(),
                healthConnectManager = healthConnectManager,
                cb = { isInit = true }
            )
        }
    }

    fun updateToPrevDate(healthConnectManager: HealthConnectManager){
        viewModelScope.launch {
            val prevDate = _uiState.value.date.minusDays(1)
            fetchSleepSessionRecord(date = prevDate, healthConnectManager = healthConnectManager)
        }
    }

    fun updateToNextDate(healthConnectManager: HealthConnectManager){
        viewModelScope.launch {
            val nextDate = _uiState.value.date.plusDays(1)
            fetchSleepSessionRecord(date = nextDate, healthConnectManager = healthConnectManager)
        }
    }

    private fun fetchSleepSessionRecord(
        date: LocalDate,
        healthConnectManager: HealthConnectManager,
        cb: (() -> Unit)? = null
    ){
        viewModelScope.launch {
            val calendarDate = convertCalendarDate(date)
            val startTime = ZonedDateTime.of(calendarDate.year, calendarDate.month, calendarDate.day, 0, 0, 0, 0, ZoneId.of("Asia/Seoul"))
            val endTime = ZonedDateTime.of(calendarDate.year, calendarDate.month, calendarDate.day, 23, 59, 59, 0, ZoneId.of("Asia/Seoul"))
            val sleepSessionRecordList = healthConnectManager.readSleepSessionRecordList(startTime.toInstant(), endTime.toInstant())

            logSleepSessionList(
                startTime = startTime,
                endTime = endTime,
                sleepSessionRecordList = sleepSessionRecordList
            )

            cb?.invoke()

            _uiState.update { prevState ->
                prevState.copy(date = date, sleepSessionRecordList = sleepSessionRecordList)
            }
        }
    }
}

data class MyProfileUiState(
    val date: LocalDate,
    val sleepSessionRecordList: List<SleepSessionRecord>? = null,
)

data class CalendarDate(
    val year: Int,
    val month: Int,
    val day: Int,
    val dayOfWeek: String
)

fun convertDayOfWeekToString(date: LocalDate): String {
    val dayOfWeek = date.dayOfWeek
    return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
}

fun convertCalendarDate(date: LocalDate): CalendarDate {
    val year = date.year
    val month = date.monthValue
    val day = date.dayOfMonth
    val dayOfWeek = convertDayOfWeekToString(date)
    return CalendarDate(year, month, day, dayOfWeek)
}

private fun logSleepSessionList(
    startTime: ZonedDateTime,
    endTime: ZonedDateTime,
    sleepSessionRecordList: List<SleepSessionRecord>?
){
    if (sleepSessionRecordList == null) {
        Log.d(TAG, "MIMO가 감지 중")
        Log.d(TAG, "${dateFormatter.format(startTime)} ~ ${dateFormatter.format(endTime)} 까지 수면기록 없음")
        return
    }

    sleepSessionRecordList.forEachIndexed() { sessionIndex, session ->
        val koreanStartTime = dateFormatter.format(session.startTime)
        val koreanEndTime = dateFormatter.format(session.endTime)
        Log.d(TAG, "@@@@@@@ 상세 수면 기록 @@@@@@@")
        Log.d(TAG, "수면 ${sessionIndex + 1} 전체 : $koreanStartTime ~ $koreanEndTime")
        session.stages.forEach() { stage ->
            Log.d(TAG, "${dateFormatter.format(stage.startTime)} ~ ${dateFormatter.format(stage.endTime)} @@ ${meanStage(stage.stage)}")
        }
    }
}
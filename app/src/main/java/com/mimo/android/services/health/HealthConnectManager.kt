package com.mimo.android.services.health

import android.content.Context
import android.os.RemoteException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.io.IOException
import java.time.Instant
import java.util.UUID


class HealthConnectManager(private val context: Context) {
    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }

    val permissions = setOf(
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
    )

    private var permissionsGranted = mutableStateOf(false)

    val requestPermissionActivityContract by lazy { PermissionController.createRequestPermissionResultContract() }

    private var uiState: UiState by mutableStateOf(UiState.Uninitialized)

    suspend fun hasAllPermissions(): Boolean {
        return healthConnectClient.permissionController.getGrantedPermissions()
            .containsAll(permissions)
    }

    suspend fun readSteps(start: Instant, end: Instant): Long {
        val request = ReadRecordsRequest(
            recordType = StepsRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end),
        )
        val response = healthConnectClient.readRecords(request)
        if (response.records.isNotEmpty()) {
            val stepRecord = response.records.last()
            return stepRecord.count
        }
        return -1
    }

//    suspend fun readWeight(start: Instant, end: Instant): Double {
//        val request = ReadRecordsRequest(
//            recordType = WeightRecord::class,
//            timeRangeFilter = TimeRangeFilter.between(start, end),
//        )
//        val response = healthConnectClient.readRecords(request)
//        if (response.records.isNotEmpty()) {
//            val weightRecord = response.records.last()
//            return weightRecord.weight.inKilograms
//        }
//        return 0.0
//    }
//
//    suspend fun readHeight(start: Instant, end: Instant): Double {
//        val request = ReadRecordsRequest(
//            recordType = HeightRecord::class,
//            timeRangeFilter = TimeRangeFilter.between(start, end),
//        )
//        val response = healthConnectClient.readRecords(request)
//        if (response.records.isNotEmpty()) {
//            val heightRecord = response.records.last()
//            return heightRecord.height.inMeters
//        }
//        return 0.0
//    }
//
//    suspend fun readBodyFatRecord(start: Instant, end: Instant): Double {
//        val request = ReadRecordsRequest(
//            recordType = BodyFatRecord::class,
//            timeRangeFilter = TimeRangeFilter.between(start, end),
//        )
//        val response = healthConnectClient.readRecords(request)
//        if (response.records.isNotEmpty()) {
//            val bodyFatRecord = response.records.last()
//            return bodyFatRecord.percentage.value
//        }
//        return 0.0
//    }
//
//    suspend fun readBasalMetabolicRateRecord(start: Instant, end: Instant): Double {
//        val request = ReadRecordsRequest(
//            recordType = BasalMetabolicRateRecord::class,
//            timeRangeFilter = TimeRangeFilter.between(start, end),
//        )
//        val response = healthConnectClient.readRecords(request)
//        if (response.records.isNotEmpty()) {
//            val basalMetabolicRateRecordRecord = response.records.last()
//            return basalMetabolicRateRecordRecord.basalMetabolicRate.inKilocaloriesPerDay
//        }
//        return 0.0
//    }

    suspend fun tryWithPermissionsCheck(block: suspend () -> Unit) {
        permissionsGranted.value = hasAllPermissions()
        uiState = try {
            if (permissionsGranted.value) {
                block()
            }
            UiState.Done
        } catch (remoteException: RemoteException) {
            UiState.Error(remoteException)
        } catch (securityException: SecurityException) {
            UiState.Error(securityException)
        } catch (ioException: IOException) {
            UiState.Error(ioException)
        } catch (illegalStateException: IllegalStateException) {
            UiState.Error(illegalStateException)
        }
    }

    sealed class UiState {
        object Uninitialized : UiState()
        object Done : UiState()
        data class Error(val exception: Throwable, val uuid: UUID = UUID.randomUUID()) : UiState()
    }

    var availability = mutableStateOf(HealthConnectAvailability.NOT_SUPPORTED)
        private set
}

enum class HealthConnectAvailability {
    INSTALLED,
    NOT_INSTALLED,
    NOT_SUPPORTED
}

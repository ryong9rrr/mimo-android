package com.mimo.android.views.main.sleep

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mimo.android.components.*
import com.mimo.android.components.base.Size
import com.mimo.android.ui.theme.Teal100
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

    LaunchedEffect(Unit) {
        sleepViewModel.fetchGetWakeupTime()
    }

    Column {
        HeadingLarge(text = "수면과 기상", fontSize = Size.lg)
        Spacer(modifier = Modifier.padding(14.dp))

        if (currentHouseNickname != null) {
            Row {
                HeadingSmall(text = currentHouseNickname, color = Teal100, fontSize = Size.xs)
                HeadingSmall(text = "에서", fontSize = Size.xs)
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        HeadingSmall(text = "기상 30 전 부터 깨워드릴게요")

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            TimeInput(
                state = state,
                modifier = Modifier.padding(16.dp),
                colors = TimePickerDefaults.colors(
                    // TODO
                )
            )
        }

        if (isActiveSleepForegroundService) {
            Button(text = "수면 끝", onClick = onStopSleepForegroundService)
        } else {
            Button(text = "수면 시작", onClick = onStartSleepForegroundService)
        }
        Spacer(modifier = Modifier.weight(2f))
        Spacer(modifier = Modifier.padding(44.dp))
    }
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


//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DatePickerCompose() {
//    val state = rememberDatePickerState()
//    val openDialog = remember { mutableStateOf(true) }
//
//    if (openDialog.value) {
//        DatePickerDialog(
//            onDismissRequest = {
//                openDialog.value = false
//            },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        openDialog.value = false
//                    }
//                ) {
//                    Text("OK")
//                }
//            },
//            dismissButton = {
//                TextButton(
//                    onClick = {
//                        openDialog.value = false
//                    }
//                ) {
//                    Text("CANCEL")
//                }
//            },
//            colors = DatePickerDefaults.colors()
//        ) {
//            DatePicker(
//                state = state
//            )
//        }
//    }
//}
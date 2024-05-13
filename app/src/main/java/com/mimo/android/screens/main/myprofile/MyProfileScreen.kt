package com.mimo.android.screens.main.myprofile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mimo.android.viewmodels.AuthViewModel
import com.mimo.android.components.Button
import com.mimo.android.components.HeadingLarge
import com.mimo.android.components.base.Size
import com.mimo.android.services.health.HealthConnectManager

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyProfileScreen(
    navController: NavHostController,
    healthConnectManager: HealthConnectManager,
    authViewModel: AuthViewModel
){
    Column {
        HeadingLarge(text = "내 정보", fontSize = Size.lg)
        Spacer(modifier = Modifier.padding(14.dp))

        // TODO: 임시
        Button(text = "로그아웃", onClick = {
            authViewModel.logout()
        })
    }

//    var stepCount by remember {
//        mutableStateOf<Long>(0)
//    }
//
//    LaunchedEffect(Unit) {
//        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
//        val now = Instant.now()
//        stepCount = healthConnectManager.readSteps(startOfDay.toInstant(), now)
//    }
//    
//    Column {
//        Text(text = "MyProfile Screen", style = TextStyle(
//            fontSize = 32.sp
//        ))
//        
//        Spacer(modifier = Modifier.padding(32.dp))
//
//        Text(text = "걸음 수 : ${stepCount}")
//    }
}
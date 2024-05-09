package com.mimo.android.screens.main.sleep

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.mimo.android.components.HeadingLarge
import com.mimo.android.components.base.Size

@Composable
fun SleepScreen(
    navController: NavHostController
){

    HeadingLarge(text = "수면,,,zzZ", fontSize = Size.lg)

}
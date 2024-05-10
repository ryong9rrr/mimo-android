package com.mimo.android.screens.main.sleep

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.mimo.android.components.*
import com.mimo.android.components.base.Size

@Composable
fun SleepScreen(
    navController: NavHostController
){

    fun handleClickButton(){

    }

    HeadingLarge(text = "수면,,,zzZ", fontSize = Size.lg)

    Button(text = "수면 시작", onClick = ::handleClickButton)
}
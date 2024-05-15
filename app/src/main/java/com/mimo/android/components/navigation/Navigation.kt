package com.mimo.android.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mimo.android.screens.MyHouseScreenDestination
import com.mimo.android.screens.MyProfileScreenDestination
import com.mimo.android.screens.SleepScreenDestination
import com.mimo.android.ui.theme.Teal900

@Preview
@Composable
fun Navigation(
    onClickMyHome: (() -> Unit)? = null,
    onClickSleep: (() -> Unit)? = null,
    onClickMyProfile: (() -> Unit)? = null,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Teal900),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Box {

        }

        Box {

        }

        Box {

        }
    }
}
package com.mimo.android.screens.main.myhouse.detaildevices

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mimo.android.apis.devices.lamp.GetLampResponse
import com.mimo.android.components.HeadingLarge
import com.mimo.android.components.HeadingSmall
import com.mimo.android.components.HorizontalScroll
import com.mimo.android.components.Icon
import com.mimo.android.components.ScrollView
import com.mimo.android.components.base.Size
import com.mimo.android.ui.theme.Teal100

@Composable
fun LampScreen(
    navController: NavHostController? = null,
    lamp: GetLampResponse? = null
){
    fun handleGoPrev() {
        navController?.navigateUp()
    }
    BackHandler {
        handleGoPrev()
    }

    if (lamp == null) {
        return
    }

    ScrollView {
        Icon(imageVector = Icons.Filled.ArrowBack, onClick = ::handleGoPrev)
        Spacer(modifier = Modifier.padding(14.dp))

        HorizontalScroll(
            children = {
                HeadingLarge(text = lamp.nickname, fontSize = Size.lg)
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        HorizontalScroll(
            children = {
                HeadingSmall(text = lamp.macAddress, fontSize = Size.sm, color = Teal100)
            }
        )
        Spacer(modifier = Modifier.padding(16.dp))

        HeadingSmall(text = "무드등 설정", fontSize = Size.lg)
        Spacer(modifier = Modifier.padding(8.dp))


    }
}
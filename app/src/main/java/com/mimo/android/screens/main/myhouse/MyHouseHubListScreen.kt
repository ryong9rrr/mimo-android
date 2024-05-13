package com.mimo.android.screens.main.myhome

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mimo.android.apis.houses.House
import com.mimo.android.components.Button
import com.mimo.android.components.ButtonSmall
import com.mimo.android.components.CardType
import com.mimo.android.components.HeadingLarge
import com.mimo.android.components.HeadingSmall
import com.mimo.android.components.HorizontalScroll
import com.mimo.android.components.Icon
import com.mimo.android.components.Modal
import com.mimo.android.components.ScrollView
import com.mimo.android.components.Text
import com.mimo.android.components.TransparentCard
import com.mimo.android.components.base.Size
import com.mimo.android.ui.theme.Gray300
import com.mimo.android.ui.theme.Gray600
import com.mimo.android.ui.theme.Teal100
import com.mimo.android.ui.theme.Teal400

@Composable
fun MyHouseHubListScreen(
    navController: NavHostController,
    house: House
){
    val hubList by remember { mutableStateOf<List<Hub>?>(null) }

    fun handleGoPrev(){
        navController.navigateUp()
    }

    BackHandler {
        handleGoPrev()
    }

    ScrollView {
        Icon(imageVector = Icons.Filled.ArrowBack, onClick = ::handleGoPrev)
        Spacer(modifier = Modifier.padding(14.dp))

        HeadingLarge(text = "허브 목록", fontSize = Size.lg)
        Spacer(modifier = Modifier.padding(8.dp))
        HorizontalScroll {
            HeadingSmall(text = "${house.nickname} / ${house.address}", fontSize = Size.sm, color = Teal100)
        }

        Spacer(modifier = Modifier.padding(16.dp))

        if (hubList == null) {
            Text(text = "Loading...")
        }
    }
}

data class Hub(
    val id: Long
)
package com.mimo.android.screens.firstsettingfunnels

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mimo.android.components.Button
import com.mimo.android.components.HeadingLarge
import com.mimo.android.components.HeadingSmall
import com.mimo.android.components.Icon
import com.mimo.android.components.Text
import com.mimo.android.components.base.Size
import com.mimo.android.ui.theme.Gray600
import com.mimo.android.ui.theme.Teal100

@Composable
fun FunnelCheckEnteredHubInfo(
    locationAlias: String,
    location: String,
    goPrev: () -> Unit,
    goStartScreen: () -> Unit,
    onConfirm: () -> Unit
){

    BackHandler {
        goPrev.invoke()
        return@BackHandler
    }

    Column {
        Icon(imageVector = Icons.Filled.ArrowBack, onClick = goPrev)
        Spacer(modifier = Modifier.padding(14.dp))

        HeadingLarge(text = "아래 정보로", fontSize = Size.lg)
        Spacer(modifier = Modifier.padding(4.dp))
        HeadingLarge(text = "허브를 등록할까요?", fontSize = Size.lg)
        
        Spacer(modifier = Modifier.padding(16.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "주소 별칭")

            HeadingSmall(text = locationAlias, fontSize = Size.sm, color = Teal100)
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "주소")

            HeadingSmall(text = location, fontSize = Size.sm, color = Teal100)
        }

        Spacer(modifier = Modifier.weight(1f))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Button(
                    text = "처음으로", color = Gray600, hasBorder = false,
                    onClick = goStartScreen
                )
            }
            item {
                Button(text = "네 등록할게요", onClick = onConfirm)
            }
        }
    }
}

@Preview
@Composable
fun FunnelCheckEnteredHubInfoPreview() {
    FunnelCheckEnteredHubInfo(
        location = "서울특별시 강남구 테헤란로 212",
        locationAlias = "서울특별시 집",
        goPrev = {},
        goStartScreen = {},
        onConfirm = {}
    )
}
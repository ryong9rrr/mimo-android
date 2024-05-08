package com.mimo.android.screens.firstsettingfunnels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mimo.android.components.HeadingLarge
import com.mimo.android.components.HeadingSmall
import com.mimo.android.components.base.Size
import com.mimo.android.ui.theme.Teal100

@Composable
fun RedirectMainAfterRegisterHubAndLocation(
    locationAlias: String,
    location: String,
    goNext: () -> Unit
){
    LaunchedEffect(Unit) {
        goNext()
    }
    
    Column {
        Spacer(modifier = Modifier.padding(30.dp))

        HeadingLarge(text = locationAlias, color = Teal100, fontSize = Size.lg)
        Spacer(modifier = Modifier.padding(8.dp))
        HeadingSmall(text = location, color = Teal100, fontSize = Size.sm)
        Spacer(modifier = Modifier.padding(16.dp))
        HeadingLarge(text = "허브와 주소를 등록했어요!", fontSize = Size.lg)
        Spacer(modifier = Modifier.padding(4.dp))
        HeadingLarge(text = "메인 화면으로 이동할게요", fontSize = Size.lg)

    }
}

@Preview
@Composable
fun RedirectMainAfterRegisterHubAndLocationPreview(){
    RedirectMainAfterRegisterHubAndLocation(
        locationAlias = "서울특별시 집",
        location = "서울특별시 강남구 테헤란로 212",
        goNext = {}
    )
}
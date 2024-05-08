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
import com.mimo.android.components.base.Size

@Composable
fun RedirectLocationRegisterAfterFindNewHub(
    goNext: () -> Unit
){

    LaunchedEffect(Unit) {
        goNext()
    }

    Column {
        Spacer(modifier = Modifier.padding(30.dp))

        HeadingLarge(text = "새로운 허브를 발견했어요!", fontSize = Size.lg)
        Spacer(modifier = Modifier.padding(4.dp))
        HeadingLarge(text = "주소 등록 화면으로 이동할게요", fontSize = Size.lg)
    }
}

@Preview
@Composable
fun RedirectLocationRegisterAfterFindNewHubPreview() {
    RedirectLocationRegisterAfterFindNewHub(
        goNext = {}
    )
}
package com.mimo.android.screens.firstsettingfunnels

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.mimo.android.components.*
import com.mimo.android.components.base.*

@Composable
fun FunnelQrCodeScan(
    goPrev: () -> Unit,
    checkCameraPermission: (() -> Unit)? = null,
){
    BackHandler {
        goPrev.invoke()
        return@BackHandler
    }

    Column {
        Icon(imageVector = Icons.Filled.ArrowBack, onClick = goPrev)
        Spacer(modifier = Modifier.padding(14.dp))

        HeadingLarge(text = "허브를 등록할게요", fontSize = Size.lg)

        Spacer(modifier = Modifier.weight(1f))
        Button(text = "QR코드 스캔", onClick = checkCameraPermission)
    }
}

@Preview
@Composable
fun FunnelQrCodeScanPreview(){
    FunnelQrCodeScan(
        goPrev = {}
    )
}
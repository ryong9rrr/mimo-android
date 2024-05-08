package com.mimo.android.screens.firstsettingfunnels

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mimo.android.components.FunnelInput
import com.mimo.android.components.HeadingLarge
import com.mimo.android.components.Icon
import com.mimo.android.components.base.Size

// TODO: 근데 굳이 위치를 따로 입력받아야하나...
@Composable
fun FunnelEnterLocationToRegisterHub(
    goPrev: () -> Unit,
    onSelectLocation: () -> Unit // TODO: 위도,경도를 보내는게 맞겠지?
){

    var inputText by remember { mutableStateOf("") }

    fun handleChange(newText: String){
        inputText = newText
    }

    BackHandler {
        goPrev.invoke()
        return@BackHandler
    }

    Column {
        Icon(imageVector = Icons.Filled.ArrowBack, onClick = goPrev)
        Spacer(modifier = Modifier.padding(14.dp))

        HeadingLarge(text = "허브를 등록할", fontSize = Size.lg)
        Spacer(modifier = Modifier.padding(4.dp))
        HeadingLarge(text = "위치를 검색해주세요", fontSize = Size.lg)

        Spacer(modifier = Modifier.padding(8.dp))

        FunnelInput(
            text = inputText,
            onChange = { newText ->  handleChange(newText) },
            onClear = { handleChange("") },
            placeholder = "주소 또는 장소를 입력해주세요",
            description = "주소 입력",
        )

        // TODO: data fetch lazy ScrollView
    }
}

@Preview
@Composable
fun FunnelEnterLocationToRegisterHubPreview(){
    FunnelEnterLocationToRegisterHub(
        goPrev = {},
        onSelectLocation = {}
    )
}
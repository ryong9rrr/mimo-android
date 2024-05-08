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
import com.mimo.android.components.Button
import com.mimo.android.components.FunnelInput
import com.mimo.android.components.HeadingLarge
import com.mimo.android.components.HeadingSmall
import com.mimo.android.components.Icon
import com.mimo.android.components.base.Size
import com.mimo.android.ui.theme.Teal100

@Composable
fun FunnelMakeLocationAlias(
    location: String,
    goPrev: () -> Unit,
    onComplete: (aliasText: String) -> Unit
){
    var inputText by remember { mutableStateOf("") }

    fun handleChange(newText: String){
        inputText = newText
    }

    fun handleComplete(){
        if (inputText.isEmpty()) {
            return
        }
        onComplete.invoke(inputText)
    }

    BackHandler {
        goPrev.invoke()
        return@BackHandler
    }

    Column {
        Icon(imageVector = Icons.Filled.ArrowBack, onClick = goPrev)
        Spacer(modifier = Modifier.padding(14.dp))

        HeadingSmall(text = location, color = Teal100, fontSize = Size.sm)
        Spacer(modifier = Modifier.padding(16.dp))
        HeadingLarge(text = "주소의 별칭을 지어주세요", fontSize = Size.lg)
        
        Spacer(modifier = Modifier.padding(8.dp))

        FunnelInput(
            text = inputText,
            onChange = { newText ->  handleChange(newText) },
            onClear = { handleChange("") },
            placeholder = setPlaceholder(location),
            description = "장소 별칭",
        )

        Spacer(modifier = Modifier.weight(1f))
        Button(text = "다음", onClick = ::handleComplete)
    }
}

fun setPlaceholder(location: String): String {
    val locationStringList = location.split(" ")
    if (locationStringList.isEmpty()) {
        return "우리집"
    }
    return "${locationStringList[0]} 집"
}

@Preview
@Composable
fun FunnelMakeLocationAliasPreview(){
    FunnelMakeLocationAlias(
        location = "서울특별시 강남구 테헤란로 212",
        goPrev = {},
        onComplete = {}
    )
}
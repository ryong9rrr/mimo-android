package com.mimo.android.ui.myicons.myiconpack

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import com.mimo.android.ui.myicons.MyIconPack
import com.mimo.android.ui.theme.Teal100
import com.mimo.android.ui.theme.Teal900

@Preview
@Composable
fun MoonIconPreview(){
    Surface {
        Image(
            imageVector = MyIconPack.MoonIcon224573,
            contentDescription = null,
            modifier = Modifier.background(color = Teal900),
            colorFilter = ColorFilter.tint(Teal100)
        )
    }
}
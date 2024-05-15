package com.mimo.android.ui.myicons.myiconpack

import androidx.compose.foundation.Image
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import com.mimo.android.ui.myicons.MyIconPack
import com.mimo.android.ui.theme.*

@Preview
@Composable
fun MoonStarsIcon(){
    Surface {
        Image(
            imageVector = MyIconPack.MoonStarsIcon184818,
            contentDescription = null,
//            modifier = Modifier.background(color = Teal900),
            colorFilter = ColorFilter.tint(Teal400)
        )
    }
}
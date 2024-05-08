package com.mimo.android.screens.login

import com.mimo.android.R
import com.mimo.android.components.*
import com.mimo.android.components.base.Size
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Preview
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    onLoginWithKakao: (() -> Unit)? = null,
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadingLarge(text = "우리 집 미라클모닝", fontSize = Size.lg)
        HeadingLarge(text = "MIMO", fontSize = Size.lg)

        Spacer(modifier = Modifier.padding(10.dp))

        SocialLoginButton(
            onClick = onLoginWithKakao,
            desc = "KaKao",
            imageResource = R.drawable.kakao_login_large_narrow
        )
    }
}

@Composable
fun SocialLoginButton(
    onClick: (() -> Unit)? = null,
    desc: String,
    imageResource: Int
){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "${desc} Login Button",
            modifier = Modifier
                .width(330.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp)) // border-radius를 16dp로 설정
                .clickable { onClick?.invoke() }
        )
    }
}
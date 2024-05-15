package com.mimo.android.components.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.mimo.android.components.navigation.myicons.MyIconPack
import com.mimo.android.components.navigation.myicons.myiconpack.MoonStarsFillIcon185549
import com.mimo.android.ui.theme.*

@Composable
fun Navigation(){

    val activeColor: Color = Teal100
    val inactiveColor: Color = Teal500

    BottomAppBar(
        cutoutShape = CircleShape,
        backgroundColor = Teal900,
        elevation = 3.dp
    ) {
        BottomNavigationItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "MyHome",
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp),
                    tint = activeColor
                )
            },
            label = {
                Text(text = "우리 집", modifier = Modifier.padding(top = 14.dp), color = activeColor)
            },
            alwaysShowLabel = true,
            enabled = true
        )

        BottomNavigationItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = {},
            enabled = false
        )

        BottomNavigationItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "MyProfile",
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp),
                    tint = inactiveColor
                )
            },
            label = {
                Text(text = "나의 정보", modifier = Modifier.padding(top = 14.dp), color = inactiveColor)
            },
            alwaysShowLabel = true,
            enabled = true
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
private fun NavigationPreview(){
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        bottomBar = { Navigation() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                contentColor = Teal100,
                backgroundColor = Teal900,
                modifier = Modifier.width(80.dp).height(80.dp)
            ) {
                Icon(
                    imageVector = MyIconPack.MoonStarsFillIcon185549,
                    contentDescription = null,
                    modifier = Modifier.height(45.dp).width(45.dp)
                )
            }
        },
        scaffoldState = scaffoldState,
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center
    ){}
}
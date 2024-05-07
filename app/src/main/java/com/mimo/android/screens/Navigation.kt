package com.mimo.android.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mimo.android.ui.theme.Teal900

/**
 * Provides the navigation in the app.
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(
    navController: NavHostController
) {

    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Teal900)
    ) {
        Button(
            contentPadding = PaddingValues(0.dp),
            shape = MaterialTheme.shapes.small,
            onClick = {
                navController.navigate(Screen.MyHomeScreen.route) {
                    popUpTo(0)
                }
            }
        ) {
            Text(text = "우리집")
        }

        Button(
            contentPadding = PaddingValues(0.dp),
            shape = MaterialTheme.shapes.small,
            onClick = {
                navController.navigate(Screen.SleepScreen.route) {
                    popUpTo(0)
                }
            }
        ) {
            Text(text = "수면")
        }

        Button(
            contentPadding = PaddingValues(0.dp),
            shape = MaterialTheme.shapes.small,
            onClick = {
                navController.navigate(Screen.MyProfileScreen.route) {
                    popUpTo(0)
                }
            }
        ) {
            Text(text = "내정보")
        }
    }
}

@Preview
@Composable
fun NavigationPreview(){
    Navigation(navController = NavHostController(LocalContext.current))
}
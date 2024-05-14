package com.mimo.android.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mimo.android.ui.theme.Teal900

private const val TAG = "Navigation"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(
    navController: NavHostController
) {

    val scope = rememberCoroutineScope()
    val currentBackStack by navController.currentBackStackEntryAsState()
    // Fetch your currentDestination:
    val currentDestination = currentBackStack?.destination
    val currentRoute = currentDestination?.route
    Log.d(TAG, "$currentRoute")

    Box {
        if (!isShowNavigation(currentRoute)) {
            return@Box
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Teal900)
        ) {
            Button(
                contentPadding = PaddingValues(0.dp),
                shape = MaterialTheme.shapes.small,
                onClick = {
                    navController.navigate(MyHouseScreenDestination.route) {
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
                    navController.navigate(SleepScreenDestination.route) {
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
                    navController.navigate(MyProfileScreenDestination.route) {
                        popUpTo(0)
                    }
                }
            ) {
                Text(text = "내정보")
            }
        }
    }
}

fun isShowNavigation(currentRoute: String?): Boolean{
    if (currentRoute == null) {
        return true
    }

    if (currentRoute.contains("ChangeHouseNicknameScreenDestination")) {
        return false
    }

    if (currentRoute.contains("CreateHouseConfirmScreen")) {
        return false
    }

    return true
}

@Preview
@Composable
fun NavigationPreview(){
    Navigation(navController = NavHostController(LocalContext.current))
}
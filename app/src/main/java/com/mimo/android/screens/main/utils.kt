package com.mimo.android.screens.main

import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun getScreenHeight(context: Context): Dp {
    val displayMetrics = context.resources.displayMetrics
    return displayMetrics.heightPixels.dp
}
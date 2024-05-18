package com.mimo.android.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mimo.android.ui.theme.Teal400
import com.mimo.android.ui.theme.Teal900

@Composable
fun LinearProgressbar(){
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth().height(1.dp),
        color = Teal900,
        trackColor = Teal400
    )
}
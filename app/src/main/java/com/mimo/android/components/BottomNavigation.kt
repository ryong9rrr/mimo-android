//package com.mimo.android.components
//
//import androidx.compose.ui.graphics.Path
//import androidx.compose.ui.graphics.Rect
//import androidx.compose.material3.*
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.rememberVectorPainter
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.*
//import com.mimo.android.screens.MyHouseScreenDestination
//import com.mimo.android.screens.MyProfileScreenDestination
//import com.mimo.android.screens.SleepScreenDestination
//import com.mimo.android.screens.isShowNavigation
//import com.mimo.android.ui.theme.Teal900
//
//@Preview
//@Composable
//fun BottomNavigation(
//    navigateToMyHouse: (() -> Unit)? = null,
//    navigateToSleep: (() -> Unit)? = null,
//    navigateToMyProfile: (() -> Unit)? = null
//){
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(color = Teal900)
//    ) {
//        Button(
//            contentPadding = PaddingValues(0.dp),
//            shape = MaterialTheme.shapes.small,
//            onClick = {}
//        ) {
//            androidx.compose.material.Text(text = "우리집")
//        }
//
//        Button(
//            contentPadding = PaddingValues(0.dp),
//            shape = MaterialTheme.shapes.small,
//            onClick = {}
//        ) {
//            Text(text = "수면")
//        }
//
//        Button(
//            contentPadding = PaddingValues(0.dp),
//            shape = MaterialTheme.shapes.small,
//            onClick = {}
//        ) {
//            Text(text = "내정보")
//        }
//    }
//}
//
//@Composable
//fun CircleShape() {
//    Canvas(
//        modifier = Modifier
//            .size(100.dp)
//            .padding(16.dp)
//    ) {
//        drawCircle(
//            color = Color.Red,
//            radius = size.minDimension / 2f,
//            center = Offset(size.width / 2f, size.height / 2f)
//        )
//    }
//}
//
//@Composable
//fun MoonIcon(
//    modifier: Modifier = Modifier,
//    color: Color = Color.Gray
//) {
//    Icon(
//        painter = rememberVectorPainter(image = { size ->
//            Canvas(size = size) {
//                drawPath(
//                    path = Path().apply {
//                        moveTo(0f, size.height / 2f)
//                        arcTo(
//                            rect = Rect(left = 0f, top = 0f, right = size.width, bottom = size.height),
//                            startAngleDegrees = 180f,
//                            sweepAngleDegrees = 180f,
//                            forceMoveTo = false
//                        )
//                    },
//                    color = color
//                )
//            }
//        }),
//        contentDescription = null,
//        tint = color,
//        modifier = modifier
//    )
//}
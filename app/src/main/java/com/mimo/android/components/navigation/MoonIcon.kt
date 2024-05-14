//package com.mimo.android.components.navigation
//
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.Icon
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Path
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.graphics.vector.rememberVectorPainter
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun MoonIcon(
//    modifier: Modifier = Modifier,
//    color: Color = Color.Gray
//) {
//    Icon(
//        painter = rememberVectorPainter(image = { size ->
//            Canvas(size = size) {
//                val radius = size.minDimension / 2f
//                val path = Path().apply {
//                    moveTo(size.width / 2f, 0f)
//                    arcTo(
//                        rect = android.graphics.RectF(0f, 0f, size.width, size.height),
//                        startAngle = 180f,
//                        sweepAngle = 180f,
//                        forceMoveTo = false
//                    )
//                }
//                drawPath(
//                    path = path,
//                    color = color,
//                    style = Stroke(width = radius * 0.5f)
//                )
//            }
//        }),
//        contentDescription = null,
//        tint = color,
//        modifier = modifier.size(24.dp)
//    )
//}
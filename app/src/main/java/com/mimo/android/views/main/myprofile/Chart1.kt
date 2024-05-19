package com.mimo.android.views.main.myprofile

import android.content.Context
import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate


@Preview
@Composable
fun SleepPatternChart(startHour: Float = 0f, endHour: Float = 14f) {
    val context = LocalContext.current

    // 차트 데이터 생성
    val lineData = remember { generateSleepPatternData() }

    AndroidView(
        modifier = Modifier.height(400.dp).fillMaxWidth(),
        factory = { ctx: Context ->
            LineChart(ctx).apply {
                data = lineData
                description.isEnabled = false
                setTouchEnabled(false)
                setPinchZoom(false)
                setDrawGridBackground(false)

                // X축 설정
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    valueFormatter = TimeAxisValueFormatter()
                    granularity = 1f
                    axisMinimum = startHour
                    axisMaximum = endHour
                }

                // Y축 설정
                axisLeft.apply {
                    setDrawGridLines(false)
                    granularity = 1f
                    axisMinimum = 0f
                    axisMaximum = 3f
                    valueFormatter = SleepStageValueFormatter()
                }
                axisRight.isEnabled = false

                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                legend.orientation = Legend.LegendOrientation.VERTICAL
                legend.setDrawInside(false)
                legend.form = Legend.LegendForm.LINE
                legend.textSize = 12f

                animateX(1500)
            }
        }
    )
}

private fun generateSleepPatternData(): LineData {
    // 예제 데이터 생성
    val entries = listOf(
        Entry(0f, 2f), Entry(1f, 3f), Entry(2f, 1f), Entry(3f, 0f), Entry(4f, 2f),
        Entry(5f, 1f), Entry(6f, 3f), Entry(7f, 2f), Entry(8f, 0f), Entry(9f, 1f),
        Entry(10f, 3f), Entry(11f, 0f), Entry(12f, 2f), Entry(13f, 1f), Entry(14f, 3f)
    )

    val dataSet = LineDataSet(entries, "Sleep Stages").apply {
        color = ColorTemplate.getHoloBlue()
        lineWidth = 2f
        setDrawCircles(true)
        setCircleColor(ColorTemplate.getHoloBlue())
        setDrawValues(false)
        mode = LineDataSet.Mode.CUBIC_BEZIER
    }

    return LineData(dataSet)
}

// X축 값 포맷터
class TimeAxisValueFormatter : com.github.mikephil.charting.formatter.ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val hour = value.toInt() / 2
        val minute = if (value.toInt() % 2 == 0) "00" else "30"
        return "$hour:$minute"
    }
}

// Y축 값 포맷터
class SleepStageValueFormatter : com.github.mikephil.charting.formatter.ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return when (value.toInt()) {
            0 -> "수면 중 깸"
            1 -> "렘 수면"
            2 -> "얕은 수면"
            3 -> "깊은 수면"
            else -> ""
        }
    }
}

import com.mimo.android.R
import android.content.Context
import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import androidx.compose.ui.graphics.Color as ComposeColor

@Preview
@Composable
fun SleepPatternChart(
    startHour: Float = 0f,
    endHour: Float = 22f
) {
    val context = LocalContext.current

    // 차트 데이터 생성
    val lineData = remember { generateSleepPatternData(context) }

    AndroidView(
        modifier = Modifier.height(300.dp).fillMaxWidth(),
        factory = { ctx: Context ->
            LineChart(ctx).apply {
                setBackgroundColor(Color.TRANSPARENT) // 배경색 설정
                data = lineData
                description.isEnabled = false
                setTouchEnabled(false)
                setPinchZoom(false)
                setDrawGridBackground(false)

                // X축 설정
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = Color.WHITE
                    valueFormatter = TimeAxisValueFormatter()
                    granularity = 1f
                    axisMinimum = 23f
                    axisMaximum = 7f
                }

                // Y축 설정
                axisLeft.apply {
                    setDrawGridLines(true)
                    setDrawAxisLine(false)
                    granularity = 1f
                    axisMinimum = 0f
                    axisMaximum = 3f
                    valueFormatter = SleepStageValueFormatter()
                    textColor = Color.WHITE
                    gridColor = Color.GRAY
                }
                axisRight.isEnabled = false

                legend.isEnabled = false // 범례 비활성화
                animateX(1500)
            }
        }
    )
}

private fun generateSleepPatternData(context: Context): LineData {
    // 예제 데이터 생성
    val entries = listOf(
        Entry(0f, 2f), Entry(1f, 3f), Entry(2f, 1f), Entry(3f, 0f), Entry(4f, 2f),
    )
//    val entries = listOf(
//        Entry(0f, 2f), Entry(1f, 3f), Entry(2f, 1f), Entry(3f, 0f), Entry(4f, 2f),
//        Entry(5f, 1f), Entry(6f, 3f), Entry(7f, 2f), Entry(8f, 0f), Entry(9f, 1f),
//        Entry(10f, 3f), Entry(11f, 0f), Entry(12f, 2f), Entry(13f, 1f), Entry(14f, 3f)
//    )

    val dataSet = LineDataSet(entries, "Sleep Stages").apply {
        color = Color.WHITE  // 선의 색상 설정
        lineWidth = 2f  // 선의 두께 설정
        setDrawCircles(false)  // 데이터 포인트에 원 그리지 않기
        setDrawValues(false)  // 데이터 값 텍스트로 표시하지 않기
        mode = LineDataSet.Mode.CUBIC_BEZIER  // 선을 곡선으로 표시
        fillAlpha = 10  // 채우기 색상 투명도 설정
        setDrawFilled(true)  // 채우기 색상 사용
        fillDrawable = ContextCompat.getDrawable(context, R.drawable.gradient) // 그라데이션 드로어블 설정
        setHighLightColor(Color.RED)  // 강조 표시 색상 설정
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

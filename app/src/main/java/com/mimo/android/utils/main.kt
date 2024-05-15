package com.mimo.android.utils

import android.widget.Toast
import com.mimo.android.MainActivity
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun showToast(text: String){
    Toast.makeText(
        MainActivity.getMainActivityContext(),
        text,
        Toast.LENGTH_SHORT
    ).show()
}

fun alertError(){
    Toast.makeText(
        MainActivity.getMainActivityContext(),
        "오류가 발생했습니다",
        Toast.LENGTH_SHORT
    ).show()
}

val dateFormatter = DateTimeFormatter.ofPattern("HH:mm")
    .withZone(ZoneId.of("Asia/Seoul"))

fun convertStringDateToKoreaTime(stringDate: String): String {
    val parsedTime = ZonedDateTime.parse(stringDate)

    // 한국 시간대로 변환
    val koreaTime = parsedTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"))

    // 날짜 형식 지정
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss")

    // 포맷팅된 문자열 생성
    return koreaTime.format(formatter)
}
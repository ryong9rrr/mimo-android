package com.mimo.android.utils

import android.widget.Toast
import com.mimo.android.MainActivity

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
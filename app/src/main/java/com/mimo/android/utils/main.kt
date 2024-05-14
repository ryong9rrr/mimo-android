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
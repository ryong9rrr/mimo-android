package com.mimo.android.services.gogglelocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mimo.android.MainActivity
import com.mimo.android.UserLocation
import java.util.Locale

@SuppressLint("MissingPermission")
fun MainActivity.launchGoogleLocationAndAddress(
    cb: (userLocation: UserLocation?) -> Unit
) {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { success: Location? ->
            success?.let { location ->
                val address = getAddress(
                    lat = location.latitude,
                    lng = location.longitude,
                    context = this
                )
                cb(UserLocation(
                    location = location,
                    address = address
                ))
            }
        }
        .addOnFailureListener { fail ->
            cb(null)
        }
}

private fun getAddress(lat: Double, lng: Double, context: Context): String? {
    val geocoder = Geocoder(context, Locale.KOREA)
    val address = geocoder.getFromLocation(lat, lng, 1)
    if (!address.isNullOrEmpty()) {
        return removeFirstWord(address[0].getAddressLine(0).toString())
    }
    return null
}

private fun removeFirstWord(input: String): String {
    val words = input.split(" ") // 공백을 기준으로 문자열을 분할하여 리스트로 만듭니다.
    return words.drop(1).joinToString(" ") // 첫 번째 단어를 제외한 나머지 단어를 결합합니다.
}
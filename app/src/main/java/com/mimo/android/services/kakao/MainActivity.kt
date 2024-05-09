package com.mimo.android.services.kakao

import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.mimo.android.MainActivity

const val kakaoAppKey = "303842984e2eb78954da62cc3e5b7a65"

fun MainActivity.initializeKakaoSdk(context: Context){
    // TODO: KAKAO SDK 초기화, key 환경변수로 관리 필요(AndroidManifest도)
    KakaoSdk.init(context, kakaoAppKey)
}
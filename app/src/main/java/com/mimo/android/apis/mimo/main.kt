package com.mimo.android.apis.mimo

import com.mimo.android.apis.common.createApiService

private const val MIMO_BASE_URL = "https://k10a204.p.ssafy.io/api/v1/"
lateinit var mimoApiService: MimoApiService;

fun mimoServiceInit(){
    mimoApiService = createApiService<MimoApiService>(MIMO_BASE_URL)
}
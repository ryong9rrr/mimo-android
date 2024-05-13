package com.mimo.android.apis

import com.mimo.android.apis.houses.HousesApiService
import com.mimo.android.apis.hubs.HubsApiService
import com.mimo.android.apis.sleeps.SleepsApiService
import com.mimo.android.apis.users.UsersApiService

interface MimoApiService: UsersApiService, HousesApiService, HubsApiService, SleepsApiService
package com.mimo.android.components.devices

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mimo.android.apis.houses.Device
import com.mimo.android.components.*
import com.mimo.android.components.base.Size
import com.mimo.android.utils.preferences.USER_ID
import com.mimo.android.utils.preferences.getData
import com.mimo.android.viewmodels.convertDeviceTypeToKoreaName
import com.mimo.android.viewmodels.isCurtainType
import com.mimo.android.viewmodels.isLampType
import com.mimo.android.viewmodels.isLightType
import com.mimo.android.viewmodels.isWindowType

@Composable
fun MyDeviceList(
    myDeviceList: List<Device>?,
    onToggleDevice: ((deviceId: Long) -> Unit)? = null,
    onClickNavigateToDetailDeviceScreen: ((device: Device) -> Unit)? = null,
){

    fun handleToggleDevice(deviceId: Long){
        onToggleDevice?.invoke(deviceId)
    }

    if (myDeviceList == null) {
        return
    }

    if (myDeviceList.isEmpty()) {
        Text(text = "등록된 허브가 없어요.")
        return
    }

    Column {
        myDeviceList.forEachIndexed { index, device ->
            TransparentCard(
                borderRadius = 8.dp,
                children = {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CardType(text = convertDeviceTypeToKoreaName(device.type))
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                size = 24.dp,
                                onClick = { onClickNavigateToDetailDeviceScreen?.invoke(device) }
                            )
                        }
                        Spacer(modifier = Modifier.padding(6.dp))

                        HorizontalScroll {
                            HeadingSmall(text = device.nickname, fontSize = Size.sm)
                        }
                        Spacer(modifier = Modifier.padding(8.dp))

                        if ( isLightType(device.type) || isLampType(device.type) ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Switch(value = false, onToggle = { handleToggleDevice(device.deviceId) })
                            }
                        }

                        if ( isCurtainType(device.type) ) {
                            RangeController(leftDesc = "어둡게", rightDesc = "밝게")
                        }

                        if ( isWindowType(device.type) ) {
                            RangeController(leftDesc = "닫힘", rightDesc = "열림")
                        }

                        Spacer(modifier = Modifier.padding(4.dp))
                    }
                }
            )

            if (index < myDeviceList.size - 1) {
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Preview
@Composable
private fun MyDeviceListPreview(){
    MyDeviceList(myDeviceList = fakeGetMyDeviceList())
}

fun fakeGetMyDeviceList(): List<Device>{
    return mutableListOf(
        Device(
            userId = getData(USER_ID)!!.toLong(),
            hubId = 1,
            deviceId = 2,
            type = "조명",
            nickname = "수지의 기깔난 조명",
            isAccessible = true,
            curColor = 30,
            openDegree = 50
        ),
        Device(
            userId = getData(USER_ID)!!.toLong(),
            hubId = 1,
            deviceId = 3,
            type = "무드등",
            nickname = "무드등",
            isAccessible = true,
            curColor = 30,
            openDegree = 50
        ),
        Device(
            userId = getData(USER_ID)!!.toLong(),
            hubId = 1,
            deviceId = 4,
            type = "커튼",
            nickname = "커튼",
            isAccessible = true,
            curColor = 30,
            openDegree = 50
        ),
        Device(
            userId = getData(USER_ID)!!.toLong(),
            hubId = 1,
            deviceId = 5,
            type = "창문",
            nickname = "창문",
            isAccessible = true,
            curColor = 30,
            openDegree = 50
        )
    )
}
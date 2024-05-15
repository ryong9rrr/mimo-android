package com.mimo.android.components.devices

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mimo.android.apis.houses.Device
import com.mimo.android.components.*
import com.mimo.android.components.base.Size
import com.mimo.android.ui.theme.*

@Composable
fun MyDeviceList(
    myDeviceList: List<Device>?,
    onToggleDevice: ((deviceId: Long) -> Unit)? = null
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
                            CardType(text = device.type)
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                size = 24.dp
                            )
                        }
                        Spacer(modifier = Modifier.padding(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = device.nickname, fontSize = Size.lg)
                                Spacer(modifier = Modifier.padding(2.dp))
                            }
                            Switch(
                                // TODO: 상태관리
                                checked = true,
                                onCheckedChange = { handleToggleDevice(device.deviceId) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Teal100, // 체크된 상태의 썸 색상 변경
                                    uncheckedThumbColor = Teal300, // 체크되지 않은 상태의 썸 색상 변경
                                    checkedTrackColor = Teal600, // 체크된 상태의 트랙 색상 변경
                                    uncheckedTrackColor = Teal600 // 체크되지 않은 상태의 트랙 색상 변경
                                )
                            )
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
            userId = -1,
            hubId = 1,
            deviceId = 2,
            type = "조명",
            nickname = "수지의 기깔난 조명",
            isAccessible = true,
            curColor = 30,
            openDegree = 50
        ),
        Device(
            userId = -1,
            hubId = 1,
            deviceId = 3,
            type = "무드등",
            nickname = "무드등",
            isAccessible = true,
            curColor = 30,
            openDegree = 50
        ),
        Device(
            userId = -1,
            hubId = 1,
            deviceId = 4,
            type = "커튼",
            nickname = "커튼",
            isAccessible = true,
            curColor = 30,
            openDegree = 50
        ),
        Device(
            userId = -1,
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
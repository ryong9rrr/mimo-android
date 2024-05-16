package com.mimo.android.screens.main.myhouse

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mimo.android.apis.houses.*
import com.mimo.android.components.*
import com.mimo.android.components.base.Size
import com.mimo.android.components.devices.*
import com.mimo.android.screens.*
import com.mimo.android.ui.theme.*
import com.mimo.android.viewmodels.*

@Composable
fun MyHouseDetailScreen(
    navController: NavHostController,
    house: House,
    myHouseDetailViewModel: MyHouseDetailViewModel,
    qrCodeViewModel: QrCodeViewModel,
    checkCameraPermissionHubToHouse: () -> Unit,
    checkCameraPermissionMachineToHub: () -> Unit,
    myHouseCurtainViewModel: MyHouseCurtainViewModel,
    myHouseLampViewModel: MyHouseLampViewModel,
    myHouseLightViewModel: MyHouseLightViewModel,
    myHouseWindowViewModel: MyHouseWindowViewModel
){
    val devices = myHouseDetailViewModel.getDevices()
    val myDeviceList = devices.myDeviceList
    val anotherDeviceList = devices.anotherDeviceList
    var isShowScreenModal by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        myHouseDetailViewModel.fetchGetDeviceListByHouseId(house.houseId)
    }

    fun handleGoPrev(){
        navController.navigateUp()
    }
    BackHandler {
        handleGoPrev()
    }

    fun navigateToDetailDeviceScreen(device: Device){
        if (isWindowType(device.type)) {
            navController.navigate("${MyHouseWindowScreenDestination.route}/${device.deviceId}")
            return
        }
        if (isLightType(device.type)) {
            navController.navigate("${MyHouseLightScreenDestination.route}/${device.deviceId}")
            return
        }
        if (isLampType(device.type)) {
            navController.navigate("${MyHouseLampScreenDestination.route}/${device.deviceId}")
            return
        }
        if (isCurtainType(device.type)) {
            navController.navigate("${MyHouseCurtainScreenDestination.route}/${device.deviceId}")
            return
        }
    }

    fun navigateToChangeHouseNicknameScreen(){
        navController.navigate("${ChangeHouseNicknameScreenDestination.route}/${house.houseId}")
    }

    fun handleShowScreenModal(){
        isShowScreenModal = true
    }

    fun handleCloseScreenModal(){
        isShowScreenModal = false
    }

    fun handleClickAddHubModalButton(){
        // TODO : 이 집에 허브 추가하고 뷰 업데이트 (뷰 업데이트 때문에 viewModel을 인자로 받아야할듯;;)
        qrCodeViewModel.initRegisterHubToHouse(houseId = house.houseId)
        checkCameraPermissionHubToHouse()
    }

    fun handleClickShowHubListButton(){
        navController.navigate("${MyHouseHubListScreenDestination.route}/${house.houseId}")
    }

    fun handleToggleMyDevice(deviceId: Long){
        myHouseDetailViewModel.fetchToggleMyDevice(deviceId)
    }

    ScrollView {
        if (isShowScreenModal) {
            Modal(
                onClose = ::handleCloseScreenModal,
                children = {
                    ScreenModalContent(
                        house = house,
                        onClose = { handleCloseScreenModal() },
                        onClickAddHubModalButton = { handleClickAddHubModalButton() },
                        onClickChangeHouseNicknameButton = { navigateToChangeHouseNicknameScreen() },
                        onClickShowHubListButton = { handleClickShowHubListButton() }
                    )
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, onClick = ::handleGoPrev)
            Icon(
                imageVector = Icons.Default.Menu,
                size = 32.dp,
                onClick = ::handleShowScreenModal
            )
        }

        Spacer(modifier = Modifier.padding(14.dp))
        HorizontalScroll(
            children = {
                HeadingLarge(text = house.nickname, fontSize = Size.lg)
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        HorizontalScroll(
            children = {
                HeadingSmall(text = house.address, fontSize = Size.sm, color = Teal100)
            }
        )

        Spacer(modifier = Modifier.padding(12.dp))

        // 현재 서비스 시스템 상 위치 등록은 현재 위치만 등록가능하므로 현재 거주지가 아니라면 그냥 기기추가 못하게 버튼 숨겨버리기
        if (!house.isHome) {
            HeadingSmall(text = "나의 기기", fontSize = Size.lg)
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HeadingSmall(text = "나의 기기", fontSize = Size.lg)
                ButtonSmall(text = "기기 추가")
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))

        if (myDeviceList.isEmpty()) {
            Text(text = "등록된 기기가 없어요. 기기를 등록해주세요.")
        } else {
            MyDeviceList(
                myDeviceList = myDeviceList,
                onToggleDevice = { deviceId -> handleToggleMyDevice(deviceId) },
                onClickNavigateToDetailDeviceScreen = { device -> navigateToDetailDeviceScreen(device) }
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))

        HeadingSmall(text = "다른 사람의 기기")
        Spacer(modifier = Modifier.padding(4.dp))
        if (anotherDeviceList.isEmpty()) {
            Text(text = "등록된 기기가 없어요.")
        } else {
            anotherDeviceList.forEachIndexed { index, device ->
                TransparentCard(
                    borderRadius = 8.dp,
                    children = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        ) {
                            CardType(text = convertDeviceTypeToKoreaName(device.type))
                            Spacer(modifier = Modifier.padding(4.dp))
                            HorizontalScroll(
                                children = {
                                    Text(text = device.nickname, fontSize = Size.lg)
                                }
                            )
                        }
                    }
                )
                if (index < anotherDeviceList.size - 1) {
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

@Composable
fun ScreenModalContent(
    house: House,
    onClose: () -> Unit,
    onClickAddHubModalButton: () -> Unit,
    onClickChangeHouseNicknameButton: () -> Unit,
    onClickShowHubListButton: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Gray300,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = Gray300,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (house.nickname.length > 20) {
                HorizontalScroll(
                    children = {
                        HeadingSmall(text = house.nickname)
                    }
                )
            } else {
                HeadingSmall(text = house.nickname)
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                Button(text = "집 별칭 변경", onClick = { onClickChangeHouseNicknameButton() })
                Spacer(modifier = Modifier.padding(4.dp))
                Button(text = "허브 목록", onClick = { onClickShowHubListButton() })
                Spacer(modifier = Modifier.padding(4.dp))
                Button(text = "허브 등록", onClick = { onClickAddHubModalButton() })
                Spacer(modifier = Modifier.padding(4.dp))
                Button(
                    text = "닫기", color = Gray600, hasBorder = false,
                    onClick = { onClose() }
                )
            }
        }
    }
}
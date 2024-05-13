package com.mimo.android.screens.main.myhouse

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mimo.android.apis.houses.House
import com.mimo.android.viewmodels.QrCodeViewModel
import com.mimo.android.components.*
import com.mimo.android.components.base.Size
import com.mimo.android.screens.*
import com.mimo.android.ui.theme.Gray300
import com.mimo.android.ui.theme.Gray600
import com.mimo.android.ui.theme.Teal100
import com.mimo.android.viewmodels.MyHouseViewModel

private const val TAG = "MyHomeScreen"

@Composable
fun MyHouseScreen(
    navController: NavHostController,
    myHouseViewModel: MyHouseViewModel,
    qrCodeViewModel: QrCodeViewModel? = null,
    checkCameraPermissionHubToHouse: (() -> Unit)? = null,
    checkCameraPermissionMachineToHub: (() -> Unit)? = null,
){
    val myHouseUiState by myHouseViewModel.uiState.collectAsState()
    var isShowModal by remember { mutableStateOf(false) }
    var selectedHouse by remember { mutableStateOf<House?>(null) }

    LaunchedEffect(Unit) {
        myHouseViewModel.fetchHouseList()
    }

    ScrollView {
        val currentHouse = myHouseViewModel.getCurrentHouse(myHouseUiState)
        val anotherHouseList = myHouseViewModel.getAnotherHouseList(myHouseUiState)

        fun navigateToMyHouseDetailScreen(house: House){
            navController.navigate("${MyHouseDetailScreenDestination.route}/${house.id}")
        }

        fun handleShowModal(house: House){
            isShowModal = true
            selectedHouse = house
        }

        fun handleCloseModal(){
            isShowModal = false
            selectedHouse = null
        }

        fun handleClickChangeCurrentHouseModalButton(house: House){
            handleCloseModal()
            if (currentHouse?.id == house.id) {
                return
            }
            myHouseViewModel.changeCurrentHome(house)
        }

        fun handleClickAddHubModalButton(house: House){
            handleCloseModal()
            qrCodeViewModel?.initRegisterHubToHouse(houseId = house.id)
            checkCameraPermissionHubToHouse?.invoke()
        }

        if (isShowModal && selectedHouse != null) {
            Modal(
                onClose = ::handleCloseModal,
                children = {
                    ModalContent(
                        isCurrentHouse = currentHouse?.id == selectedHouse!!.id,
                        house = selectedHouse!!,
                        onClose = ::handleCloseModal,
                        onClickChangeCurrentHouseModalButton = ::handleClickChangeCurrentHouseModalButton,
                        onClickAddHubModalButton = ::handleClickAddHubModalButton
                    )
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            HeadingLarge(text = "우리 집", fontSize = Size.lg)
            ButtonSmall(text = "거주지 추가")
        }

        Spacer(modifier = Modifier.padding(14.dp))

        HeadingSmall(text = "현재 거주지", fontSize = Size.lg)
        Spacer(modifier = Modifier.padding(4.dp))


        if (currentHouse == null) {
            Text(text = "등록된 거주지가 없어요")
        }
        if (currentHouse != null) {
            Card(
                house = currentHouse,
                isCurrentHome = true,
                onClick = { house -> navigateToMyHouseDetailScreen(house) },
                onClickMenu = { house -> handleShowModal(house) },
                onLongClick = {}
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        HeadingSmall(text = "다른 거주지")
        Spacer(modifier = Modifier.padding(4.dp))
        if (anotherHouseList.isEmpty()) {
            Text(text = "등록된 거주지가 없어요")
        }
        if (anotherHouseList.isNotEmpty()) {
            anotherHouseList.forEachIndexed { index, anotherHouse ->
                Card(
                    house = anotherHouse,
                    isCurrentHome = false,
                    onClick = { house -> navigateToMyHouseDetailScreen(house) },
                    onClickMenu = { house -> handleShowModal(house) },
                    onLongClick = {}
                )

                if (index < anotherHouseList.size) {
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Card(
    house: House,
    isCurrentHome: Boolean,
    onClick: ((house: House) -> Unit)? = null,
    onLongClick: ((house: House) -> Unit)? = null,
    onClickMenu: ((house: House) -> Unit)? = null
){
    Box(
        modifier = Modifier.combinedClickable(
            onClick = { onClick?.invoke(house) },
            onLongClick = {
                if (!isCurrentHome) {
                    onLongClick?.invoke(house)
                }
            }
        )
    ){
        TransparentCard(
            borderRadius = 8.dp,
            children = {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(96.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Row {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                house.devices.forEach { device -> CardType(text = device) }
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.Menu,
                            size = 24.dp,
                            onClick = { onClickMenu?.invoke(house) }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    HorizontalScroll {
                        HeadingSmall(text = house.nickname, Size.lg)
                    }

                    Spacer(modifier = Modifier.padding(4.dp))

                    HorizontalScroll {
                        HeadingSmall(text = house.address, fontSize = Size.xs, color = Teal100)
                    }
                }
            }
        )
    }
}

@Composable
fun ModalContent(
    isCurrentHouse: Boolean,
    house: House,
    onClose: (() -> Unit)? = null,
    onClickChangeCurrentHouseModalButton: ((house: House) -> Unit)? = null,
    onClickAddHubModalButton: ((house: House) -> Unit)? = null
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
            HeadingSmall(text = house.nickname)
//            Spacer(modifier = Modifier.padding(2.dp))
//            Text(text = "현재 거주지로 변경할까요?")
            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                if (!isCurrentHouse) {
                    Button(text = "현재 거주지로 변경", onClick = { onClickChangeCurrentHouseModalButton?.invoke(house) })
                    Spacer(modifier = Modifier.padding(4.dp))
                }
                Button(text = "허브 등록", onClick = { onClickAddHubModalButton?.invoke(house) })
                Spacer(modifier = Modifier.padding(4.dp))
                Button(
                        text = "닫기", color = Gray600, hasBorder = false,
                        onClick = { onClose?.invoke() }
                    )
            }
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(2),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                item {
//                    Button(
//                        text = "닫기", color = Gray600, hasBorder = false,
//                        onClick = { onClose?.invoke() }
//                    )
//                }
//                item {
//                    Button(text = "변경할게요", onClick = { onConfirm?.invoke(home) })
//                }
//            }
        }
    }
}

@Preview
@Composable
private fun MyHouseScreenPreview(){
    val navController = NavHostController(LocalContext.current)
    val houseList: List<House> = arrayListOf(
        House(
            id = 1,
            isHome = true,
            devices = arrayListOf("조명", "무드등"),
            nickname = "상윤이의 자취방",
            address = "서울특별시 관악구 봉천동 1234-56"
        ),
        House(
            id = 2,
            isHome = false,
            devices = arrayListOf("조명", "창문", "커튼"),
            nickname = "상윤이의 본가",
            address = "경기도 고양시 일산서구 산현로12"
        ),
        House(
            id = 3,
            isHome = false,
            devices = arrayListOf("조명", "커튼"),
            nickname = "싸피",
            address = "서울특별시 강남구 테헤란로 212"
        )
    )

    val myHouseViewModel = MyHouseViewModel()
    myHouseViewModel.updateHouseList(houseList)

    MyHouseScreen(
        navController = navController,
        myHouseViewModel = myHouseViewModel
    )
}
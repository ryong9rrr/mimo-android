package com.mimo.android.screens.main.myhouse

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mimo.android.apis.houses.House
import com.mimo.android.components.*
import com.mimo.android.ui.theme.Gray300
import com.mimo.android.ui.theme.Gray600
import com.mimo.android.viewmodels.MyHouseDetailViewModel

@Composable
fun MyHouseDetailScreen(
    navController: NavHostController,
    house: House,
    myHouseDetailViewModel: MyHouseDetailViewModel,
//    isCurrentHome: Boolean,
//    myItems: Any,
//    anotherPeopleItems: Any,
//    qrCodeViewModel: QrCodeViewModel? = null,
//    checkCameraPermissionHubToHouse: (() -> Unit)? = null,
//    checkCameraPermissionMachineToHub: (() -> Unit)? = null,
){
//    val houseId = home.homeId!!
//    var isShowScreenModal by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        myHouseDetailViewModel.fetchGetDeviceListByHouseId(house.houseId)
    }

    fun handleGoPrev(){
        navController.navigateUp()
    }
    BackHandler {
        handleGoPrev()
    }





//    fun handleShowScreenModal(){
//        isShowScreenModal = true
//    }
//
//    fun handleCloseScreenModal(){
//        isShowScreenModal = false
//    }
//
//    fun handleClickAddHubModalButton(){
//        // TODO : 이 집에 허브 추가하고 뷰 업데이트 (뷰 업데이트 때문에 viewModel을 인자로 받아야할듯;;)
//        qrCodeViewModel?.initRegisterHubToHouse(houseId = houseId)
//        checkCameraPermissionHubToHouse?.invoke()
//    }
//
//    fun handleClickChangeHouseNicknameButton(){
//        // TODO: HomeNicknameChangeScreen으로 navigate
//    }
//
//    fun handleClickShowHubListButton(){
//        // TODO: HomeHubListScreen으로 navigate
//        navController.navigate("${HomeHubListScreen.route}/${home.homeId}")
//    }



    ScrollView {

//        if (isShowScreenModal) {
//            Modal(
//                onClose = ::handleCloseScreenModal,
//                children = {
//                    ScreenModalContent(
//                        home = home,
//                        onClose = { handleCloseScreenModal() },
//                        onClickAddHubModalButton = { handleClickAddHubModalButton() },
//                        onClickChangeHouseNicknameButton = { handleClickChangeHouseNicknameButton() },
//                        onClickShowHubListButton = { handleClickShowHubListButton() }
//                    )
//                }
//            )
//        }
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//        ) {
//            Icon(imageVector = Icons.Filled.ArrowBack, onClick = ::handleGoPrev)
//            Icon(
//                imageVector = Icons.Default.Menu,
//                size = 32.dp,
//                onClick = ::handleShowScreenModal
//            )
//        }
//
//        Spacer(modifier = Modifier.padding(14.dp))
//        HorizontalScroll {
//            HeadingLarge(text = home.homeName, fontSize = Size.lg)
//        }
//        Spacer(modifier = Modifier.padding(4.dp))
//        HorizontalScroll {
//            HeadingSmall(text = home.address, fontSize = Size.sm, color = Teal100)
//        }
//
//        Spacer(modifier = Modifier.padding(12.dp))
//        if (!isCurrentHome) {
//            HeadingSmall(text = "나의 기기", fontSize = Size.lg)
//        }
//        if (isCurrentHome) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.Bottom,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                HeadingSmall(text = "나의 기기", fontSize = Size.lg)
//                ButtonSmall(text = "기기 추가") // TODO: "현재 거주지" 에서만 기기추가 가능
//            }
//        }
//        Spacer(modifier = Modifier.padding(8.dp))
//        // TODO: 기기 Card
//        // Text(text = "등록된 기기가 없어요. 기기를 등록해주세요.")
//        TransparentCard(
//            borderRadius = 8.dp,
//            children = {
//                Column(
//                    modifier = Modifier.padding(8.dp)
//                ) {
//                    Row (
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        CardType(text = "조명")
//                        Icon(
//                            imageVector = Icons.Filled.KeyboardArrowRight,
//                            size = 24.dp
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.padding(4.dp))
//
//                    HorizontalScroll {
//                        Text(text = "수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명", fontSize = Size.lg)
//                    }
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center
//                    ){
//
//                        var value by remember { mutableStateOf(50f) }
//
//                        Text(text = "어둡게")
//
//                        Spacer(modifier = Modifier.weight(1f))
//
//                        Slider(
//                            value = value, // 초기 값
//                            onValueChange = { nextValue ->
//
////                                CoroutineScope(Dispatchers.Main).launch {
////                                    nextValue
////                                        // 디바운스를 적용하여 슬라이더 값 변경 이벤트를 지연시킵니다.
////                                        .debounce(300)
////                                        // collectLatest를 사용하여 가장 최신 값을 수신합니다.
////                                        .collectLatest { value ->
////                                            // 디바운스된 값으로 작업을 수행합니다.
////                                            handleSliderValue(value)
////                                        }
////                                }
//
//                                value = nextValue
//                            },
//                            valueRange = 0f..100f, // 슬라이더 값 범위
//                            steps = 10000, // 슬라이더의 이동 단위
//                            modifier = Modifier.width(260.dp), // 슬라이더의 너비 지정
//                            colors = SliderDefaults.colors(
//                                activeTickColor = Teal400,
//                                inactiveTickColor = Color.Gray,
//                                thumbColor = Teal400, // 슬라이더 썸 색상
//                                activeTrackColor = Teal400, // 활성화된 슬라이더 트랙의 색상
//                                inactiveTrackColor = Color.Gray // 비활성화된 슬라이더 트랙의 색상
//                            ),
//                            // 옵션 설명
////                            value: Float, // 슬라이더의 현재 값.
////                            onValueChange: (Float) -> Unit, // 슬라이더 값이 변경될 때 호출되는 콜백 함수.
////                            valueRange: ClosedFloatingPointRange<Float>, // 슬라이더 값의 범위를 지정하는 범위 객체.
////                            steps: Int = 0, // 슬라이더의 이동 단위. 값이 0보다 큰 경우, 슬라이더는 주어진 값에 따라 고정된 단계로 이동합니다.
////                            onValueChangeFinished: (() -> Unit)? = null, // 슬라이더 값을 변경한 후 호출되는 콜백 함수.
////                            colors: SliderColors = SliderDefaults.colors(), // 슬라이더의 색상을 지정하는 객체.
////                            modifier: Modifier = Modifier, // Modifier를 사용하여 슬라이더에 적용할 수 있는 수정자.
////                            enabled: Boolean = true, // 슬라이더가 활성화되었는지 여부를 나타내는 부울 값.
////                            onGloballyPositioned: OnGloballyPositionedModifier = null, // 슬라이더가 전역 위치를 설정할 때 호출되는 콜백 함수.
////                            interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }, // 슬라이더의 상호 작용 소스를 지정하는 객체.
////                            onValueChangeInteractionSource: MutableInteractionSource = interactionSource, // 슬라이더 값 변경 시 상호 작용 소스를 지정하는 객체.
////                            onValueChangeFinishedInteractionSource: MutableInteractionSource = interactionSource, // 슬라이더 값 변경 후 상호 작용 소스를 지정하는 객체.
////                            )
//                        )
//                        Spacer(modifier = Modifier.weight(1f))
//                        Text(text = "어둡게")
//                    }
//                }
//            }
//        )
//
//        Spacer(modifier = Modifier.padding(16.dp))
//
//        HeadingSmall(text = "다른 사람의 기기")
//        Spacer(modifier = Modifier.padding(4.dp))
//        // TODO
//        // Text(text = "등록된 기기가 없어요.")
//        TransparentCard(
//            borderRadius = 8.dp,
//            children = {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                ) {
//                    CardType(text = "조명")
//                    Spacer(modifier = Modifier.padding(4.dp))
//                    HorizontalScroll {
//                        Text(text = "수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명", fontSize = Size.lg)
//                    }
//                }
//            }
//        )
//        Spacer(modifier = Modifier.padding(4.dp))
//        TransparentCard(
//            borderRadius = 8.dp,
//            children = {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                ) {
//                    CardType(text = "조명")
//                    Spacer(modifier = Modifier.padding(4.dp))
//                    HorizontalScroll {
//                        Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
//                    }
//                }
//            }
//        )
//        Spacer(modifier = Modifier.padding(4.dp))
//        TransparentCard(
//            borderRadius = 8.dp,
//            children = {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                ) {
//                    CardType(text = "조명")
//                    Spacer(modifier = Modifier.padding(4.dp))
//                    HorizontalScroll {
//                        Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
//                    }
//                }
//            }
//        )
//        Spacer(modifier = Modifier.padding(4.dp))
//        TransparentCard(
//            borderRadius = 8.dp,
//            children = {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                ) {
//                    CardType(text = "조명")
//                    Spacer(modifier = Modifier.padding(4.dp))
//                    HorizontalScroll {
//                        Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
//                    }
//                }
//            }
//        )
//        Spacer(modifier = Modifier.padding(4.dp))
//        TransparentCard(
//            borderRadius = 8.dp,
//            children = {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                ) {
//                    CardType(text = "조명")
//                    Spacer(modifier = Modifier.padding(4.dp))
//                    HorizontalScroll {
//                        Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
//                    }
//                }
//            }
//        )
//        Spacer(modifier = Modifier.padding(4.dp))
//        TransparentCard(
//            borderRadius = 8.dp,
//            children = {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                ) {
//                    CardType(text = "조명")
//                    Spacer(modifier = Modifier.padding(4.dp))
//                    HorizontalScroll {
//                        Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
//                    }
//                }
//            }
//        )
//        Spacer(modifier = Modifier.padding(4.dp))
//        TransparentCard(
//                borderRadius = 8.dp,
//        children = {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//            ) {
//                CardType(text = "조명")
//                Spacer(modifier = Modifier.padding(4.dp))
//                HorizontalScroll {
//                    Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
//                }
//            }
//        }
//        )
//        Spacer(modifier = Modifier.padding(4.dp))
//        TransparentCard(
//            borderRadius = 8.dp,
//            children = {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                ) {
//                    CardType(text = "조명")
//                    Spacer(modifier = Modifier.padding(4.dp))
//                    HorizontalScroll {
//                        Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
//                    }
//                }
//            }
//        )
    }
}

@Composable
fun ScreenModalContent(
    house: House,
    onClose: (() -> Unit)? = null,
    onClickAddHubModalButton: (() -> Unit)? = null,
    onClickChangeHouseNicknameButton: (() -> Unit)? = null,
    onClickShowHubListButton: (() -> Unit)? = null
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
            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                Button(text = "집 이름 바꾸기", onClick = { onClickChangeHouseNicknameButton?.invoke() })
                Spacer(modifier = Modifier.padding(4.dp))
                Button(text = "허브 등록하기", onClick = { onClickAddHubModalButton?.invoke() })
                Spacer(modifier = Modifier.padding(4.dp))
                Button(text = "허브 목록 보기", onClick = { onClickShowHubListButton?.invoke() })
                Spacer(modifier = Modifier.padding(4.dp))
                Button(
                    text = "닫기", color = Gray600, hasBorder = false,
                    onClick = { onClose?.invoke() }
                )
            }
        }
    }
}

//@Preview
//@Composable
//private fun MyHomeDetailScreenPreview(){
//    val navController = NavHostController(LocalContext.current)
//    val home = Home(
//        homeId = 1,
//        homeName = "낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가",
//        address = "서울특별시 관악구 봉천동 1234-56",
//        devices = arrayListOf("조명", "커튼")
//    )
//
//    MyHomeDetailScreen(
//        navController = navController,
//        home = home,
//        isCurrentHome = true,
//        myItems = Any(),
//        anotherPeopleItems = Any()
//    )
//}
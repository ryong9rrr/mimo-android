package com.mimo.android.screens.main.myhome

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mimo.android.components.*
import com.mimo.android.components.base.Size
import com.mimo.android.ui.theme.Teal100
import com.mimo.android.ui.theme.Teal400
import com.mimo.android.ui.theme.Teal800
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.debounce

@Composable
fun MyHomeDetailScreen(
    navController: NavHostController,
    home: Home,
    isCurrentHome: Boolean,
    myItems: Any,
    anotherPeopleItems: Any
){
    fun handleGoPrev(){
        navController.navigateUp()
    }

    BackHandler {
        handleGoPrev()
    }

    ScrollView {
        Icon(imageVector = Icons.Filled.ArrowBack, onClick = ::handleGoPrev)
        Spacer(modifier = Modifier.padding(14.dp))
        HorizontalScroll {
            HeadingLarge(text = home.homeName, fontSize = Size.lg)
        }
        Spacer(modifier = Modifier.padding(4.dp))
        HorizontalScroll {
            HeadingSmall(text = home.address, fontSize = Size.sm, color = Teal100)
        }

        Spacer(modifier = Modifier.padding(12.dp))
        if (!isCurrentHome) {
            HeadingSmall(text = "나의 기기", fontSize = Size.lg)
        }
        if (isCurrentHome) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HeadingSmall(text = "나의 기기", fontSize = Size.lg)
                ButtonSmall(text = "기기 추가") // TODO: "현재 거주지" 에서만 기기추가 가능
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        // TODO: 기기 Card
        // Text(text = "등록된 기기가 없어요. 기기를 등록해주세요.")
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
                        CardType(text = "조명")
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            size = 24.dp
                        )
                    }

                    Spacer(modifier = Modifier.padding(4.dp))

                    HorizontalScroll {
                        Text(text = "수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명", fontSize = Size.lg)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){

                        var value by remember { mutableStateOf(50f) }

                        Text(text = "어둡게")

                        Spacer(modifier = Modifier.weight(1f))

                        Slider(
                            value = value, // 초기 값
                            onValueChange = { nextValue ->

//                                CoroutineScope(Dispatchers.Main).launch {
//                                    nextValue
//                                        // 디바운스를 적용하여 슬라이더 값 변경 이벤트를 지연시킵니다.
//                                        .debounce(300)
//                                        // collectLatest를 사용하여 가장 최신 값을 수신합니다.
//                                        .collectLatest { value ->
//                                            // 디바운스된 값으로 작업을 수행합니다.
//                                            handleSliderValue(value)
//                                        }
//                                }

                                println(nextValue)
                                value = nextValue
                            },
                            valueRange = 0f..100f, // 슬라이더 값 범위
                            steps = 10000, // 슬라이더의 이동 단위
                            modifier = Modifier.width(260.dp), // 슬라이더의 너비 지정
                            colors = SliderDefaults.colors(
                                activeTickColor = Teal400,
                                inactiveTickColor = Color.Gray,
                                thumbColor = Teal400, // 슬라이더 썸 색상
                                activeTrackColor = Teal400, // 활성화된 슬라이더 트랙의 색상
                                inactiveTrackColor = Color.Gray // 비활성화된 슬라이더 트랙의 색상
                            ),
                            // 옵션 설명
//                            value: Float, // 슬라이더의 현재 값.
//                            onValueChange: (Float) -> Unit, // 슬라이더 값이 변경될 때 호출되는 콜백 함수.
//                            valueRange: ClosedFloatingPointRange<Float>, // 슬라이더 값의 범위를 지정하는 범위 객체.
//                            steps: Int = 0, // 슬라이더의 이동 단위. 값이 0보다 큰 경우, 슬라이더는 주어진 값에 따라 고정된 단계로 이동합니다.
//                            onValueChangeFinished: (() -> Unit)? = null, // 슬라이더 값을 변경한 후 호출되는 콜백 함수.
//                            colors: SliderColors = SliderDefaults.colors(), // 슬라이더의 색상을 지정하는 객체.
//                            modifier: Modifier = Modifier, // Modifier를 사용하여 슬라이더에 적용할 수 있는 수정자.
//                            enabled: Boolean = true, // 슬라이더가 활성화되었는지 여부를 나타내는 부울 값.
//                            onGloballyPositioned: OnGloballyPositionedModifier = null, // 슬라이더가 전역 위치를 설정할 때 호출되는 콜백 함수.
//                            interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }, // 슬라이더의 상호 작용 소스를 지정하는 객체.
//                            onValueChangeInteractionSource: MutableInteractionSource = interactionSource, // 슬라이더 값 변경 시 상호 작용 소스를 지정하는 객체.
//                            onValueChangeFinishedInteractionSource: MutableInteractionSource = interactionSource, // 슬라이더 값 변경 후 상호 작용 소스를 지정하는 객체.
//                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "어둡게")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.padding(16.dp))

        HeadingSmall(text = "다른 사람의 기기")
        Spacer(modifier = Modifier.padding(4.dp))
        // TODO
        // Text(text = "등록된 기기가 없어요.")
        TransparentCard(
            borderRadius = 8.dp,
            children = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    CardType(text = "조명")
                    Spacer(modifier = Modifier.padding(4.dp))
                    HorizontalScroll {
                        Text(text = "수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명 수지의 기깔난 조명", fontSize = Size.lg)
                    }
                }
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        TransparentCard(
            borderRadius = 8.dp,
            children = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    CardType(text = "조명")
                    Spacer(modifier = Modifier.padding(4.dp))
                    HorizontalScroll {
                        Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
                    }
                }
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        TransparentCard(
            borderRadius = 8.dp,
            children = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    CardType(text = "조명")
                    Spacer(modifier = Modifier.padding(4.dp))
                    HorizontalScroll {
                        Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
                    }
                }
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        TransparentCard(
            borderRadius = 8.dp,
            children = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    CardType(text = "조명")
                    Spacer(modifier = Modifier.padding(4.dp))
                    HorizontalScroll {
                        Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
                    }
                }
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        TransparentCard(
            borderRadius = 8.dp,
            children = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    CardType(text = "조명")
                    Spacer(modifier = Modifier.padding(4.dp))
                    HorizontalScroll {
                        Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
                    }
                }
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        TransparentCard(
            borderRadius = 8.dp,
            children = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    CardType(text = "조명")
                    Spacer(modifier = Modifier.padding(4.dp))
                    HorizontalScroll {
                        Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
                    }
                }
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        TransparentCard(
                borderRadius = 8.dp,
        children = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                CardType(text = "조명")
                Spacer(modifier = Modifier.padding(4.dp))
                HorizontalScroll {
                    Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
                }
            }
        }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        TransparentCard(
            borderRadius = 8.dp,
            children = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    CardType(text = "조명")
                    Spacer(modifier = Modifier.padding(4.dp))
                    HorizontalScroll {
                        Text(text = "수지의 기깔난 조명", fontSize = Size.lg)
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun MyHomeDetailScreenPreview(){
    val navController = NavHostController(LocalContext.current)
    val home = Home(
        homeId = "1",
        homeName = "낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가 낙성대 7번출구 어딘가",
        address = "서울특별시 관악구 봉천동 1234-56",
        items = arrayOf("조명", "커튼")
    )

    MyHomeDetailScreen(
        navController = navController,
        home = home,
        isCurrentHome = true,
        myItems = Any(),
        anotherPeopleItems = Any()
    )
}
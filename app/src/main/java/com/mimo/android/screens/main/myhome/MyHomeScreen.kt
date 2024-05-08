package com.mimo.android.screens.main.myhome

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import com.mimo.android.components.*
import com.mimo.android.components.base.Size
import com.mimo.android.screens.MyHomeDetailDestination
import com.mimo.android.ui.theme.Gray300
import com.mimo.android.ui.theme.Gray600
import com.mimo.android.ui.theme.Teal100

@Composable
fun MyHomeScreen(
    navController: NavHostController,
    myHomeViewModel: MyHomeViewModel,

){
    ScrollView {
        val myHomeUiState by myHomeViewModel.uiState.collectAsState()
        var isShowModal by remember { mutableStateOf(false) }
        var selectedHome by remember { mutableStateOf<Home?>(null) }

        fun navigateToMyHomeDetailScreen(home: Home){
            if (home.homeId != null) {
                navController.navigate("${MyHomeDetailDestination.route}/${home.homeId}")
            }
        }

        fun handleShowModal(home: Home?){
            isShowModal = true
            selectedHome = home
        }

        fun handleCloseModal(){
            isShowModal = false
            selectedHome = null
        }

        fun handleConfirmModal(home: Home){
            handleCloseModal()
            myHomeViewModel.changeCurrentHome(home.homeId)
        }

        if (isShowModal && selectedHome != null) {
            Modal(
                onClose = ::handleCloseModal,
                children = {
                    ModalContent(
                        home = selectedHome!!,
                        onClose = ::handleCloseModal,
                        onConfirm = { home -> handleConfirmModal(home) }
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
        if (myHomeUiState.currentHome == null) {
            Text(text = "등록된 거주지가 없어요")
        }
        if (myHomeUiState.currentHome != null) {
            Card(
                home = myHomeUiState.currentHome!!,
                isCurrentHome = true,
                onClick = { home -> navigateToMyHomeDetailScreen(home) },
                onLongClick = { home -> handleShowModal(home) }
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        HeadingSmall(text = "다른 거주지")
        Spacer(modifier = Modifier.padding(4.dp))
        if (myHomeUiState.anotherHomeList.isEmpty()) {
            Text(text = "등록된 거주지가 없어요")
        }
        if (myHomeUiState.anotherHomeList.isNotEmpty()) {
            myHomeUiState.anotherHomeList.forEachIndexed { index, anotherHome ->
                Card(
                    home = anotherHome,
                    isCurrentHome = false,
                    onClick = { home -> navigateToMyHomeDetailScreen(home) },
                    onLongClick = { home -> handleShowModal(home) }
                )

                if (index < myHomeUiState.anotherHomeList.size) {
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Card(
    home: Home,
    isCurrentHome: Boolean,
    onClick: ((home: Home) -> Unit)? = null,
    onLongClick: ((home: Home) -> Unit)? = null
){
    Box(
        modifier = Modifier.combinedClickable(
            onClick = { onClick?.invoke(home) },
            onLongClick = {
                if (!isCurrentHome) {
                    onLongClick?.invoke(home)
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

                    Row (
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        if (home.items != null) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                home.items.forEach { item -> CardType(text = item) }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    HorizontalScroll {
                        HeadingSmall(text = home.homeName, Size.lg)
                    }

                    Spacer(modifier = Modifier.padding(4.dp))

                    HorizontalScroll {
                        HeadingSmall(text = home.address, fontSize = Size.xs, color = Teal100)
                    }
                }
            }
        )
    }
}


@Composable
fun ModalContent(
    home: Home,
    onClose: (() -> Unit)? = null,
    onConfirm: ((home: Home) -> Unit)? = null
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
            HeadingSmall(text = home.homeName)
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "현재 거주지로 변경할까요?")
            Spacer(modifier = Modifier.padding(6.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Button(
                        text = "닫기", color = Gray600, hasBorder = false,
                        onClick = { onClose?.invoke() }
                    )
                }
                item {
                    Button(text = "변경할게요", onClick = { onConfirm?.invoke(home) })
                }
            }
        }
    }
}

@Preview
@Composable
private fun MyHomeScreenPreview(){
    val navController = NavHostController(LocalContext.current)
    val currentHome = Home(
        items = arrayOf("조명", "무드등"),
        homeName = "상윤이의 자취방",
        address = "서울특별시 관악구 봉천동 1234-56"
    )
    val anotherHomeList: List<Home> = mutableListOf(
        Home(
            items = arrayOf("조명", "창문", "커튼"),
            homeName = "상윤이의 본가",
            address = "경기도 고양시 일산서구 산현로12"
        ),
        Home(
            items = arrayOf("조명", "커튼"),
            homeName = "싸피",
            address = "서울특별시 강남구 테헤란로 212"
        )
    )

    val myHomeViewModel = MyHomeViewModel()
    myHomeViewModel.updateCurrentHome(currentHome)
    myHomeViewModel.updateAnotherHomeList(anotherHomeList)

    MyHomeScreen(
        navController = navController,
        myHomeViewModel = myHomeViewModel
    )
}
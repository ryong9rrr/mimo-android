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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
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
import com.mimo.android.components.Button
import com.mimo.android.components.ButtonSmall
import com.mimo.android.components.CardType
import com.mimo.android.components.HeadingLarge
import com.mimo.android.components.HeadingSmall
import com.mimo.android.components.Modal
import com.mimo.android.components.ScrollView
import com.mimo.android.components.Text
import com.mimo.android.components.TransparentCard
import com.mimo.android.components.base.Size
import com.mimo.android.ui.theme.Gray300
import com.mimo.android.ui.theme.Gray600
import com.mimo.android.ui.theme.Teal100
import com.mimo.android.screens.Screen

@Composable
fun MyHomeScreen(
    navController: NavHostController,
    currentHome: HubHome?,
    anotherHomes: Array<HubHome>?
){
    ScrollView (
        children = {

            var isShowModal by remember { mutableStateOf(false) }
            var selectHomeName by remember { mutableStateOf<String?>(null) }

            fun showModal(homeName: String?){
                isShowModal = true
                selectHomeName = homeName
            }

            fun closeModal(){
                isShowModal = false
                selectHomeName = null
            }

            if (isShowModal && selectHomeName != null) {
                Modal(
                    onClose = ::closeModal,
                    children = {
                        ModalContent(
                            homeName = selectHomeName,
                            onClose = ::closeModal,
                            onConfirm = {} // TODO: 현재 거주지변경 api 요청 + Toast
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

            HeadingSmall(text = "현재 거주지")
            Spacer(modifier = Modifier.padding(8.dp))

            if (currentHome == null) {
                Box(){
                    Text(text = "등록된 거주지가 없어요")
                }
            }

            if (currentHome != null) {
                Card(
                    hubHome = currentHome,
                    isCurrentHome = true,
                    onClick = { homeName -> navController.navigate(Screen.SleepScreen.route) }, // FIXME: 임시함수
                    onLongClick = { homeName -> showModal(homeName) }
                )
            }

            Spacer(modifier = Modifier.padding(16.dp))

            HeadingSmall(text = "다른 거주지")
            Spacer(modifier = Modifier.padding(8.dp))

            anotherHomes?.forEachIndexed { index, anotherHome ->
                Card(
                    hubHome = anotherHome,
                    isCurrentHome = false,
                    onClick = { homeName -> navController.navigate(Screen.SleepScreen.route) }, // FIXME: 임시함수
                    onLongClick = { homeName -> showModal(homeName) }
                )

                if (index < anotherHomes.size) {
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Card(
    hubHome: HubHome,
    isCurrentHome: Boolean,
    onClick: ((homeName: String?) -> Unit)? = null,
    onLongClick: ((homeName: String?) -> Unit)? = null
){
    Box(
        modifier = Modifier.combinedClickable(
            onClick = { onClick?.invoke(hubHome.homeName) },
            onLongClick = {
                if (!isCurrentHome) {
                    onLongClick?.invoke(hubHome.homeName)
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
                    .height(120.dp)) {
                    Row (
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        if (hubHome.items != null) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                hubHome.items.forEach { item -> CardType(text = item) }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    HeadingSmall(text = hubHome.homeName ?: "빈 집")
                    Spacer(modifier = Modifier.padding(4.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    HeadingSmall(text = hubHome.address ?: "빈 주소", fontSize = Size.xs, color = Teal100)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalContent(
    homeName: String? = null,
    onClose: (() -> Unit)? = null,
    onConfirm: (() -> Unit)? = null
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
            HeadingSmall(text = homeName ?: "")
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
                    Button(text = "변경할게요", onClick = { onConfirm?.invoke() })
                }
            }
        }
    }
}

data class HubHome(
    val items: Array<String>? = null,
    val homeName: String,
    val address: String
)

@Preview
@Composable
private fun MyHomeScreenPreview(){
    val navController = NavHostController(LocalContext.current)
    val currentHome = HubHome(
        items = arrayOf("조명", "무드등"),
        homeName = "상윤이의 자취방",
        address = "서울특별시 관악구 봉천동 1234-56"
    )
    val anotherHomes: Array<HubHome> = arrayOf(
        HubHome(
            items = arrayOf("조명", "창문", "커튼"),
            homeName = "상윤이의 본가",
            address = "경기도 고양시 일산서구 산현로12"
        ),
        HubHome(
            items = arrayOf("조명", "커튼"),
            homeName = "싸피",
            address = "서울특별시 강남구 테헤란로 212"
        )
    )

    MyHomeScreen(
        navController = navController,
        currentHome = currentHome,
        anotherHomes = anotherHomes
    )
}
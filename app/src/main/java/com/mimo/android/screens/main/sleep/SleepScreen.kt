package com.mimo.android.screens.main.sleep

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mimo.android.apis.todo.Post
import com.mimo.android.apis.todo.getPost
import com.mimo.android.apis.todo.getPosts
import com.mimo.android.components.HeadingLarge
import com.mimo.android.components.ScrollView
import com.mimo.android.components.Text
import com.mimo.android.components.base.Size

@Composable
fun SleepScreen(
    navController: NavHostController
){

    var posts by remember { mutableStateOf<List<Post>?>(null) }
    var post by remember { mutableStateOf<Post?>(null) }

    HeadingLarge(text = "수면,,,zzZ", fontSize = Size.lg)

    // 네트워크 요청을 비동기적으로 실행
    LaunchedEffect(true) {
        getPost(
            postId = "1",
            onSuccessCallback = { fetchedPost -> post = fetchedPost }
        )
        getPosts(
            onSuccessCallback = { fetchedPosts -> posts = fetchedPosts }
        )
    }



    // UI 작성
    ScrollView {
        
        Spacer(modifier = Modifier.padding(30.dp))
        
        HeadingLarge(text = "1번 Post")
        if (post == null) {
            Text(text = "Loading...")
        }
        if (post != null) {
            Text(text = "Post Title: ${post!!.title}")
        }

        Spacer(modifier = Modifier.padding(16.dp))

        HeadingLarge(text = "Post 목록")
        if (posts == null) {
            Text(text = "Loading...")
        }
        if (posts != null) {
            posts!!.forEachIndexed { index, post ->
                Text(text = "${post.id} @ ${post.title}")
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    }

}
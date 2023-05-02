package com.example.smartestsoil.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.example.smartestsoil.model.PlantsFireStorePagingSource
import com.example.smartestsoil.model.PostsFireStorePagingSource
import com.example.smartestsoil.model.UserPlant
import com.example.smartestsoil.model.UserPost
import com.example.smartestsoil.ui.screens.notes.NotesButton
import com.example.smartestsoil.viewModel.SensorViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FeedScreen(navController: NavController) {
    // Paging configuration
    val pagingConfig = PagingConfig(
        pageSize = 10,
        prefetchDistance = 5,
        enablePlaceholders = false
    )
    // State to track whether the dialog form is open
    var showDialog by remember { mutableStateOf(false) }
    // Function to handle FAB click and open the dialog form where new plant can be added
    fun onFABClick() {
        showDialog = true
    }

    // Dialog form composable for adding a plant
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            content = {
                AddPlant(onClose = { showDialog = false })
            }
        )
    }

    Column {
        val lifecycle = LocalLifecycleOwner.current.lifecycle

        val lazyPagingItems = remember {
            val db = Firebase.firestore
            val source = PostsFireStorePagingSource(db)
            val pager = Pager(config = pagingConfig, pagingSourceFactory = { source })
            pager.flow.cachedIn(lifecycle.coroutineScope)
        }.collectAsLazyPagingItems()

        LazyColumn(contentPadding = PaddingValues(8.dp)) {
            items(lazyPagingItems) { post ->
                if (post != null) {
                    PostCard(post) { clickedPost ->
                        Log.d("PlantListView", "Clicked Plant: ${clickedPost.user}")
                    }
                }
            }
        }

        FloatingActionButton(
            shape = CircleShape,
            onClick = { onFABClick() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add FAB",
                tint = MaterialTheme.colors.onPrimary
            )
        }
    }
}


@Composable
fun PostCard(post: UserPost, onClick: (UserPost) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = { onClick(post) }),
        elevation = 4.dp,
    ) {
        Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = post.media,
                            builder = {
                                crossfade(true)
                            }
                        ),
                        contentDescription = post.user,
                        contentScale = ContentScale.Crop,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = post.user,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        fontSize = 18.sp
                    )
                    Text(
                        text = post.date.toString(),
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Text(text = post.body, fontSize = 16.sp, color = MaterialTheme.colors.onSurface)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = post.media,
                            builder = {
                                crossfade(true)
                            }
                        ),
                        contentDescription = "Media",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )

                }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Heart Icon",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Text(
                        text = "${post.likes} Likes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}
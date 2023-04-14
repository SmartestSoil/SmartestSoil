package com.example.smartestsoil.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.smartestsoil.R
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.coroutineScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.example.smartestsoil.model.CurrentDateTime
import com.example.smartestsoil.model.SensorsFirestorePagingSource
import com.example.smartestsoil.model.UserSensor
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PlantListView(db: FirebaseFirestore){
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
                AddSensor(onClose = { showDialog = false })
            }
        )
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    // LazyPagingItems
    val lazyPagingItems = remember {
        val source = SensorsFirestorePagingSource(db)
        val pager = Pager(config = pagingConfig, pagingSourceFactory = { source })
        pager.flow.cachedIn(lifecycle.coroutineScope)
    }.collectAsLazyPagingItems()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { onFABClick() },
                modifier = Modifier.absolutePadding(bottom = 50.dp) // set bottom padding to move FAB on top of bottom nav bar
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add FAB",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ){
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            contentPadding = PaddingValues(24.dp),
            content = {
                items(lazyPagingItems.itemCount) { index ->
                    val sensor = lazyPagingItems[index]
                    if (sensor != null) {
                        SensorCard(sensor = sensor)
                    }
                }
            }
        )
    }
}

@Composable
fun SensorCard(sensor: UserSensor) {
    Card(
        shape = RoundedCornerShape(1.dp),
        border = BorderStroke(0.dp, color = Color.Transparent),
        modifier = Modifier
            .height(180.dp)
            .width(140.dp),
        elevation = 0.dp,
        backgroundColor = Color.Transparent
    ) {
        var showNoteDialog by remember { mutableStateOf(false) }
        val notes = remember { mutableStateListOf<String>() }

        Column( modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberImagePainter(
                    data = sensor.imageUrl,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = sensor.sensorName,

                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = sensor.sensorName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            // Tab to add a note
            Card(
                shape = RoundedCornerShape(20.dp),
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .clickable(onClick = { showNoteDialog = true })
                    .height(40.dp),
                elevation = 8.dp,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Notes Icon",
                        tint = MaterialTheme.colors.onPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Notes",
                        color = MaterialTheme.colors.onPrimary,
                        fontSize = 16.sp,
                    )
                }
            }
            if (showNoteDialog) {
                AlertDialog(
                    onDismissRequest = { showNoteDialog = false },
                    title = {
                        Text(
                            text = "Notes for ${sensor.sensorName}",
                            color = MaterialTheme.colors.primaryVariant,
                            fontSize = 20.sp
                        )},
                    text = {
                        Column {
                            notes.forEach { note ->
                                Text(text = note, fontWeight = FontWeight.Normal, fontSize = 14.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            val noteState = remember { mutableStateOf("") }
                            val colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                cursorColor = MaterialTheme.colors.primaryVariant,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent
                            )
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF2E1C5)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    CurrentDateTime()
                                }
                                TextField(
                                    modifier = Modifier.padding(16.dp),
                                    value = noteState.value,
                                    onValueChange = { noteState.value = it },
                                    textStyle =
                                    TextStyle(
                                        color = MaterialTheme.colors.primary
                                    ),
                                    singleLine = false,
                                    maxLines = 5,
                                    colors = colors,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = { /*TODO*/ }
                                    ),
                                    trailingIcon = {
                                        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.End)) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Close",
                                                tint = MaterialTheme.colors.primary
                                            )
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    notes.add(noteState.value)
                                    noteState.value = ""
                                },
                                enabled = noteState.value.isNotBlank(),
                            ) {
                                Text("Add note")
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showNoteDialog = false }) {
                            Text("Close")
                        }
                    }
                )
            }
        }
    }
}

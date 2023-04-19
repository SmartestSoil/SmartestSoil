package com.example.smartestsoil.ui.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import coil.compose.rememberImagePainter
import com.example.smartestsoil.model.PlantsFirestorePagingSource
import com.example.smartestsoil.model.UserPlant
import com.example.smartestsoil.viewModel.SensorViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PlantListView(db: FirebaseFirestore, navController: NavController, viewModel: SensorViewModel){
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

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val lazyPagingItems = remember {
        val db = Firebase.firestore
        val source = PlantsFirestorePagingSource(db)
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
                Log.d("MyApp", "itemCount: ${lazyPagingItems.itemCount}")
                items(lazyPagingItems.itemCount) { index ->
                    val plant = lazyPagingItems[index]
                    if (plant != null) {
                        PlantCard(plant, plant.pairedSensor) { clickedPlant ->
                            Log.d("PlantListView", "Clicked Plant: ${clickedPlant.plantName}")
                            val clickedPairedSensor = clickedPlant.pairedSensor
                            SensorViewModel.CurrentSensor = clickedPairedSensor
                            Log.d("PlantListView", "Clicked Paired Sensor: $clickedPairedSensor")
                            navController.navigate("home")
                        }

                    }
                }

            }
        )
    }
    }


@Composable
fun PlantCard(plant: UserPlant,pairedSensor: String,  onClick: (UserPlant) -> Unit) {
    Card(
        shape = RoundedCornerShape(1.dp),
        border = BorderStroke(0.dp, color = Color.Transparent),
        modifier = Modifier
            .height(180.dp)
            .width(140.dp)
            .clickable(onClick = {onClick(plant)} ),
        elevation = 0.dp,
        backgroundColor = Color.Transparent
    ) {
        Column( modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
            ) {
                Image(
                painter = rememberImagePainter(
                    data = plant.imageUrl,
                    builder = {
                        crossfade(true)
                    }
                ),
                    contentDescription = plant.plantName,
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = plant.plantName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = plant.pairedSensor, fontWeight = FontWeight.Bold, fontSize = 18.sp)

        }

    }
}

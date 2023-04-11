package com.example.smartestsoil.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog


@Composable
fun PlantListView() {
    val cardData = remember { mutableStateListOf(*generateFakeCards().toTypedArray()) }

    // Function to handle removing a plant card
    fun onPlantDelete(index: Int) {
        cardData.removeAt(index)
    }

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
                AddPlantImage()
                /*Button(
                    onClick = { showDialog = false }
                ) {
                    Text("OK")
                }*/
            }
        )
    }

    Box() {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(24.dp)
        ) {
            cardData.forEachIndexed { index, card ->
                item(span = { GridItemSpan(1) }) {
                    Plant(
                        moisture = card.first,
                        name = card.second,
                        location = "Location",
                        onDelete ={ onPlantDelete(index) }
                    )
                }
            }
        }
        FloatingActionButton(
            //modifier = Modifier.align(alignment = Alignment.BottomEnd),
            onClick = { onFABClick() },
            backgroundColor = MaterialTheme.colors.secondary,
            //shape = RoundedCornerShape(50.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add FAB",
                tint = Color.White,
            )
        }
    }
}

@Composable
fun Plant(
    moisture: String,
    name: String,
    location: String,
    onDelete: () -> Unit // Add a callback for delete action
) {
    Card(
        shape = RoundedCornerShape(1.dp),
        border = BorderStroke(0.dp, color = Color.Transparent),
        modifier = Modifier
            .height(180.dp)
            .width(140.dp),
        elevation = 0.dp,
        backgroundColor = Color.Transparent
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_round_white_300),
                    contentDescription = "Plant image",
                    modifier = Modifier.size(96.dp)
                )
                Row() {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                    Text(
                        text = moisture,
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = name,
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = location,
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center
                )
            }
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete plant",
                tint = Color.Red,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clickable(onClick = onDelete) // Call the callback on click
            )
        }
    }
}
private fun generateFakeCards(): List<Pair<String, String>> {
    return MutableList(20) { index ->
        val cardNumber = index + 1
        "Moisture $cardNumber" to "Name $cardNumber"
    }
}
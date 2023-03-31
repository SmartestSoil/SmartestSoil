package com.example.smartestsoil.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smartestsoil.model.PlantModel
import com.example.smartestsoil.viewModel.PlantListViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PlantListScreen(category: String) {
    val viewModel = remember { PlantListViewModel() }

    // Call the composable function outside of remember and assign the result to a variable
    val plantList = viewModel.getPlantList(LocalContext.current, category)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category) }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                if (plantList.isNotEmpty()) {
                    for (plant in plantList) {
                        Text(
                            text = plant.plantName,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                } else {
                    Text(text = "No plants found.")
                }
            }
        }
    )
}

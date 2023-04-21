package com.example.smartestsoil.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smartestsoil.model.PlantModel
import com.example.smartestsoil.viewModel.PlantListViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PlantListScreen(plantName: String) {
    val viewModel = remember { PlantListViewModel() }

    // Call the composable function outside of remember and assign the result to a variable
    val plant = viewModel.getPlantByName(LocalContext.current, plantName)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(plantName) }
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                item {
                    plant?.let {
                        Text(
                            text = "Temperature: ${it.suitableTemperature.min}°C - ${it.suitableTemperature.max}°C",
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "Moisture: ${it.suitableMoisture.min}% - ${it.suitableMoisture.max}%",
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "Care - Light: ${it.care.light}, Water: ${it.care.water}, Fertilizer: ${it.care.fertilizer}",
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "Description: ${it.description}",
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    } ?: run {
                        Text(text = "No plant found with the given name.")
                    }
                }
            }
        }
    )
}
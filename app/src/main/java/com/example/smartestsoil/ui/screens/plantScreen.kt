package com.example.smartestsoil.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.smartestsoil.R
import com.example.smartestsoil.viewModel.PlantListViewModel
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PlantListScreen(plantName: String) {
    val viewModel = remember { PlantListViewModel() }

    // Call the composable function outside of remember and assign the result to a variable
    val plant = viewModel.getPlantByName(LocalContext.current, plantName)
    Scaffold(
        topBar = {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Image(
                        painter = painterResource(R.drawable.flower3),
                        contentDescription = "Plant icon",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 8.dp, start = 8.dp)
                    )
                    Text(plantName,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp ,
                        )
                    )
                }

        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                item {
                    plant?.let {
                        Row(
                            modifier = Modifier.padding(bottom = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row {
                                Image(
                                    painter = painterResource(R.drawable.tempmeter),
                                    contentDescription = "Temperature icon",
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = "${it.suitableTemperature.min}°C - ${it.suitableTemperature.max}°C",
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Row {
                                Image(
                                    painter = painterResource(R.drawable.moisture),
                                    contentDescription = "Moisture icon",
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = " ${it.suitableMoisture.min}% - ${it.suitableMoisture.max}%",
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
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
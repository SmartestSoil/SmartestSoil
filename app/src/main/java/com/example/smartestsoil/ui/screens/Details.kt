package com.example.smartestsoil.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery0Bar
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smartestsoil.R
import com.example.smartestsoil.ui.theme.Green01


@Composable
fun Details(navController: NavController) {
    val itemsList = listOf("sensor1", "sensor2", "sensor3")
    val batteryPercentages = listOf("80%", "75%", "82%")
    val pairedDates = listOf("2023-05-01", "2023-05-02", "2023-05-03")
    val monitoredItems = listOf("Tomato, Lettuce", "Potato, Carrot", "Cabbage, Spinach")
    val selectedItem = remember { mutableStateOf(-1) }

        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items(itemsList.size) { item ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(1.0f)
                        .padding(vertical = 10.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                selectedItem.value = if (selectedItem.value == item) -1 else item
                            }
                        ),
                    shape = RoundedCornerShape(4.dp),
                    elevation = if (selectedItem.value == item) 8.dp else 4.dp, // Change elevation when clicked
                    color = MaterialTheme.colors.surface
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row {
                            Image(
                                painter = painterResource(R.drawable.moisture),
                                contentDescription = "Moisture icon",
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = itemsList[item],
                                modifier = Modifier.weight(1f),
                                color = Green01,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Add the battery icon and power percentage text
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.BatteryFull,
                                    contentDescription = "Battery icon",
                                    modifier = Modifier.size(30.dp),
                                    tint = Green01
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = batteryPercentages[item],
                                    color = Green01,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Add additional content for the clicked item
                        if (selectedItem.value == item) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Paired date: ${pairedDates[item]}",
                                color = Green01,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Monitors: ${monitoredItems[item]}",
                                color = Green01,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
package com.example.smartestsoil.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartestsoil.model.UserPlant
import com.example.smartestsoil.viewModel.SensorViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


@Composable
fun editPlantDetails(
    onClose: () -> Unit,
    onSave: (UserPlant) -> Unit,
    showDialog: MutableState<Boolean> = remember { mutableStateOf(true) }
) {
    val currentId = SensorViewModel.CurrentPlantId
    var plant =  SensorViewModel.CurrentPlant
    val user = FirebaseAuth.getInstance().currentUser
    Log.d("current screen plant ID", "$currentId; plant=${plant.toString()}")

    var plantData by remember { mutableStateOf<UserPlant?>(plant) }
    var plantName by remember { mutableStateOf(plantData?.plantName ?: "") }
    var imageUrl by remember { mutableStateOf(plantData?.imageUrl ?: "") }
    var pairedSensor by remember { mutableStateOf(plantData?.pairedSensor ?: "") }
    var plantId by remember { mutableStateOf(plantData?.plantId ?: "") }
    var userId by remember { mutableStateOf(plantData?.userId ?: "") }

    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Edit Plant",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = plantName,
                    onValueChange = { plantName = it },
                    label = { Text("Change Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Change Image") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = pairedSensor,
                    onValueChange = { pairedSensor = it },
                    label = { Text("Change Paired Sensor") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            val updatedPlant = UserPlant(
                                plantId = plantId,
                                plantName = plantName,
                                imageUrl = imageUrl,
                                pairedSensor = pairedSensor,
                                userId = (user?.uid).toString()
                            )
                            onSave(updatedPlant)
                            onClose()
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .height(48.dp)
                    ) {
                        Text("Save")
                    }

                    Button(
                        onClick = { onClose() },
                        modifier = Modifier
                            .width(120.dp)
                            .height(48.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
@Composable
fun DeletePlantDialog(showDeleteDialog:MutableState<Boolean> = remember { mutableStateOf(true) }, onDelete: (plantId: String) -> Unit, onClose: () -> Unit) {
    var plantId = SensorViewModel.CurrentPlantId
    if (showDeleteDialog.value) {
        Dialog(onDismissRequest = { showDeleteDialog.value = false }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Are you sure you want to delete this plant?",
                    fontWeight = FontWeight.Light,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            if (plantId != null) {
                                onDelete(plantId)
                            }
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .height(48.dp)
                    ) {
                        Text("Delete")
                    }

                    Button(
                        onClick = { onClose() },
                        modifier = Modifier
                            .width(120.dp)
                            .height(48.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
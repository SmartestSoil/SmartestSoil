package com.example.smartestsoil.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.example.smartestsoil.R
import com.example.smartestsoil.model.PlantModel
import com.example.smartestsoil.model.PlantsFireStorePagingSource
import com.example.smartestsoil.model.SensorData
import com.example.smartestsoil.model.UserPlant
import com.example.smartestsoil.ui.screens.authentication.TemperatureScale
import com.example.smartestsoil.viewModel.PlantListViewModel
import com.example.smartestsoil.viewModel.SensorViewModel
import java.lang.Math.round
import kotlin.math.roundToInt
import androidx.compose.animation.core.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(
    navController: NavController,
    sensorViewModel: SensorViewModel = viewModel()) {
    var showDialog by remember { mutableStateOf(false)}
    var showDeleteDialog by remember { mutableStateOf(false)}

    Scaffold(bottomBar = {
        BottomAppBar(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit")
            }

            if (showDialog) {
                editPlantDetails(
                    onClose = { showDialog = false},
                    onSave = { updatedPlant -> sensorViewModel.setPlantO(updatedPlant)
                        navController.navigate("plantlist")},
                    showDialog = remember { mutableStateOf(showDialog)}
                )
            }
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
            if(showDeleteDialog) {
                DeletePlantDialog(
                    showDeleteDialog= remember { mutableStateOf(showDeleteDialog)},
                    onDelete = { deletedPlant -> sensorViewModel.deletePlantO(deletedPlant)
                        navController.navigate("plantlist")}, //  plantId -> sensorViewModel.deletePlantO(plantId)
                    onClose = { showDeleteDialog = false })
                }
        }
    }) {
        SensorList(sensorViewModel.sensordata,  viewModel = viewModel())
       }
}
@Composable
fun SensorList(sensordata: List<SensorData>, viewModel: PlantListViewModel) {
    val context = LocalContext.current
    val currentPlantName = SensorViewModel.CurrentPlant?.plantName?: ""
    val plant = viewModel.getPlantByName(context, currentPlantName) //( from plant lib (json) VM)
    val minMoisture = plant?.suitableMoisture?.min ?: 20
    val maxMoisture = plant?.suitableMoisture?.max ?: 50
    val minTemperature = plant?.suitableTemperature?.min ?: 5
    val maxTemperature = plant?.suitableTemperature?.max ?: 40
    Log.d("data for lib","$minMoisture,$maxMoisture, $minTemperature, $maxTemperature " )
    val curSensor = SensorViewModel.CurrentSensor
    val lastStoredMoistureValue = rememberSaveable { mutableStateOf("N/A") }
    val moistureValue = sensordata.filter { sensorData ->
        when (curSensor) {
            "sensor1" -> sensorData.sensor_id == "soil_sensor_001"
            "sensor2" -> sensorData.sensor_id == "soil_sensor_002"
            "sensor3" -> sensorData.sensor_id == "soil_sensor_003"
            else -> true // include all sensors if curSensor is not sensor1 or sensor2
        }
    }.lastOrNull()?.soil_moisture ?: lastStoredMoistureValue.value
    val roundedMoistureValue = (moistureValue as? Float)?.toInt()

    if (roundedMoistureValue != null && roundedMoistureValue.toString() != "N/A") {
        lastStoredMoistureValue.value = roundedMoistureValue.toString()
    }

    val circleBackgroundColor = Color(0xFF95A178).copy(alpha = 0.5f)
    val greenColor = Color(0xFF95A178)

    //traffic light colors
    val redColor = Color(0xFFCC0000)
    val orangeColor = Color(0xFFFFA500)
    val greenColor2 = Color(0xFF70853D)
    val moistureDifference = when {
        roundedMoistureValue == null -> null
        roundedMoistureValue < minMoisture -> roundedMoistureValue - minMoisture
        roundedMoistureValue > maxMoisture -> roundedMoistureValue - maxMoisture
        else -> 0
    }
    val circleOutlineColor = when {
        moistureDifference != null && moistureDifference <= -10 -> redColor
        moistureDifference != null && moistureDifference >= 10 -> redColor
        else -> greenColor.copy(alpha = 0.5f)
    }
    if (moistureDifference != null && moistureDifference in -9..9) {
        circleOutlineColor.copy(alpha = 0.5f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(circleBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(circleOutlineColor.copy(alpha = 0.0f))
                    .border(
                        width = 10.dp,
                        brush = SolidColor(circleOutlineColor),
                        shape = CircleShape
                    )
            )

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.droplet_half),
                    contentDescription = "Droplet half",
                    modifier = Modifier
                        .size(70.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "$roundedMoistureValue%",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = greenColor
                )

        }
    }

        Spacer(modifier = Modifier.height(32.dp)) // Add some space between the boxes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            contentAlignment = Alignment.Center
        ) {

            if (curSensor != null) {
                SensorChart(sensordata,curSensor)
            }

        }
        Spacer(modifier = Modifier.height(40.dp)) // Add some space between the boxes
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 4.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { /* Handle click here */ }
                ),
            shape = RoundedCornerShape(4.dp),
            elevation = 4.dp,
            color = MaterialTheme.colors.surface
        ) {

           /* Column(modifier = Modifier.padding(20.dp)) {
                TemperatureScale(
                    minTemperature = 0,
                    maxTemperature = 50,
                    plantMinTemperature = 20,
                    plantMaxTemperature = 30,
                    sensorTemperature = 25,
                    modifier = Modifier.fillMaxSize()
                )*/

                PlantListScreen(currentPlantName)
            }





            /* LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        items(itemsList) { item ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 10.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { /* Handle item click here */ }
                    ),
                shape = RoundedCornerShape(4.dp),
                elevation = 4.dp,
                color = MaterialTheme.colors.surface
            ) {
                Row(modifier = Modifier.padding(20.dp)) {
                    Text(
                        item,
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }*/


    }

}
/*@Composable
fun PlantList(viewModel: PlantListViewModel = viewModel()) {
    val plantList = viewModel.getPlantList(LocalContext.current)

    LazyColumn {
        items(plantList) { plant ->
            Text(text = plant.plantName)
            // Add more UI elements to display other plant details
        }
    }
}*/


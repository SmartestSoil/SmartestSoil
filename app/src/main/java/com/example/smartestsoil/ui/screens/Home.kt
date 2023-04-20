package com.example.smartestsoil.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartestsoil.R
import com.example.smartestsoil.model.PlantModel
import com.example.smartestsoil.model.SensorData
import com.example.smartestsoil.viewModel.PlantListViewModel
import com.example.smartestsoil.viewModel.SensorViewModel



@Composable
fun Home(
    navController: NavController,
    sensorViewModel: SensorViewModel = viewModel()) {
    SensorList(sensorViewModel.sensordata)
}
@Composable
fun SensorList(sensordata: List<SensorData>) {
    val curSensor = SensorViewModel.CurrentSensor
    val lastStoredMoistureValue = rememberSaveable { mutableStateOf("N/A") }
    val moistureValue = sensordata.filter { sensorData ->
        when (curSensor) {
            "sensor1" -> sensorData.sensor_id == "soil_sensor_001"
            "sensor2" -> sensorData.sensor_id == "soil_sensor_002"
            else -> true // include all sensors if curSensor is not sensor1 or sensor2
        }
    }.lastOrNull()?.soil_moisture ?: lastStoredMoistureValue.value

    val circleBackgroundColor = Color(0xFF95A178).copy(alpha = 0.5f)
    val greenColor = Color(0xFF95A178)
    if (moistureValue != "N/A") {
        lastStoredMoistureValue.value = moistureValue.toString()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(circleBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.droplet_half),
                contentDescription = "Droplet half",
                modifier = Modifier
                    .size(90.dp)
                    .padding(bottom = 30.dp)
            )
            // Draw the moisture value
            Box(
                modifier = Modifier
                    .padding(top = 70.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$moistureValue%",
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
            Column(modifier = Modifier.padding(20.dp)) {
                Text("")
                PlantListScreen("popularHouseplants")
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
}}
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
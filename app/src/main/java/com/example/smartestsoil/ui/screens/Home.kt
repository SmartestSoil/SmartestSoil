package com.example.smartestsoil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartestsoil.model.SensorData
import com.example.smartestsoil.ui.theme.SmartestSoilTheme
import com.example.smartestsoil.viewModel.FirebaseAuthViewModel
import com.example.smartestsoil.viewModel.SensorViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.endAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


/*@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    SmartestSoilTheme(/*useSystemUiController = false*/) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Home()
        }
    }
}*/
@Composable
fun Home(
    navController: NavController,
    sensorViewModel: SensorViewModel = viewModel()) {

    SensorList(sensorViewModel.sensordata)
}
@Composable
fun SensorList(sensordata: List<SensorData>) {

    val currentDate = LocalDate.now(ZoneId.systemDefault())
    val dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    val lastStoredMoistureValue = rememberSaveable { mutableStateOf("N/A") }
    val moistureValue = sensordata.lastOrNull()?.soil_moisture ?: lastStoredMoistureValue.value
    val todaysData = sensordata.filter { sensorData ->
        val timestampDate = LocalDateTime.parse(sensorData.timestamp, dateFormatter).toLocalDate()
        timestampDate == currentDate
    }

    val temperatureEntries = todaysData.mapIndexed { index, sensorData ->
        FloatEntry(index.toFloat(), sensorData.soil_temperature)
    }
    val moistureEntries = todaysData.mapIndexed { index, sensorData ->
        FloatEntry(index.toFloat(), sensorData.soil_moisture)
    }
    val chartEntryModel = entryModelOf(temperatureEntries, moistureEntries)


    val itemsList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    //val chartEntryModel = entryModelOf(entriesOf(4f, 12f, 8f, 16f),
    //   entriesOf(12f, 16f, 4f, 12f))
    // Update the stored value when a new value is fetched
    if (moistureValue != "N/A") {
        lastStoredMoistureValue.value = moistureValue.toString()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            // Draw the moisture value
            Text(
                text = "$moistureValue%",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(32.dp)) // Add some space between the boxes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            contentAlignment = Alignment.Center
        ) {
            Chart(
                chart = lineChart(spacing = 20.dp),
                model = chartEntryModel,
                startAxis = startAxis(),
                bottomAxis = bottomAxis(),
                endAxis = endAxis(),
                //marker = Marker,
                isZoomEnabled = true,
            )
        }
        Spacer(modifier = Modifier.height(60.dp)) // Add some space between the boxes
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
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Your text content goes here.")
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
}
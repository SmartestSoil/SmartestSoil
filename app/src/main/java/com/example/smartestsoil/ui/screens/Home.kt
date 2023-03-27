package com.example.smartestsoil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import com.example.smartestsoil.model.SensorData
import com.example.smartestsoil.ui.theme.SmartestSoilTheme
import com.example.smartestsoil.viewModel.FirebaseAuthViewModel
import com.example.smartestsoil.viewModel.SensorViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.endAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.chart.decoration.Decoration
import com.patrykandpatrick.vico.core.chart.insets.ChartInsetter
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.marker.Marker
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Preview(showBackground = true)
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
}
@Composable
fun Home(sensorViewModel: SensorViewModel = viewModel()) {
    val firebaseAuthViewModel: FirebaseAuthViewModel = viewModel()
   /* Text(text = "this is the home")
    Button(
        onClick = { firebaseAuthViewModel.logout() }
    ) {
        Text(text = "Logout")
    }*/
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


    val chartEntryModel = entryModelOf(temperatureEntries)

    // Update the stored value when a new value is fetched
    if (moistureValue != "N/A") {
        lastStoredMoistureValue.value = moistureValue.toString()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        contentAlignment = Alignment.TopCenter
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
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp),
        contentAlignment = Alignment.Center
    ) {
        Chart(
            chart = lineChart( spacing= 20.dp ),
            model = chartEntryModel,
            startAxis = startAxis(),
            bottomAxis = bottomAxis(),
            endAxis= endAxis(),
           //marker = Marker,
            isZoomEnabled= true,

        )
    }


}

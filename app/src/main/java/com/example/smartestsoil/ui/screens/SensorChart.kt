package com.example.smartestsoil.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartestsoil.model.SensorData
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

@Composable
fun SensorChart(sensorData: List<SensorData>) {
    // move the chart-related code from the SensorList composable to this function
    val currentDate = LocalDate.now(ZoneId.systemDefault())
    val dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val todaysData = sensorData.filter { sensorData ->
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
}
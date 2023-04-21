package com.example.smartestsoil.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartestsoil.model.SensorData
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import java.time.*
import java.time.format.DateTimeFormatter


@Composable
fun SensorChart(sensorData: List<SensorData>, displaySensor: String) {
    Log.d("in view model and in sensordata ", "$displaySensor")
    val dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    val latestDateUtc = sensorData
        .map { OffsetDateTime.parse(it.timestamp, dateFormatter).toInstant().atZone(ZoneId.of("UTC")).toLocalDate() }
        .maxOrNull()

    val todaysData = sensorData.filter { sensorData ->
        val timestampDateTime = OffsetDateTime.parse(sensorData.timestamp, dateFormatter)
        val timestampDateUtc = timestampDateTime.toInstant().atZone(ZoneId.of("UTC")).toLocalDate()

        val isMatchingSensor = when (displaySensor) {
            "sensor1" -> sensorData.sensor_id == "soil_sensor_001"
            "sensor2" -> sensorData.sensor_id == "soil_sensor_002"
            else -> false // handle other cases if needed
        }
        isMatchingSensor && (timestampDateUtc == latestDateUtc)
    }

    val temperatureEntries = todaysData.mapIndexed { index, sensorData ->
        FloatEntry(index.toFloat(), sensorData.soil_temperature)
    }
    val moistureEntries = todaysData.mapIndexed { index, sensorData ->
        FloatEntry(index.toFloat(), sensorData.soil_moisture)
    }
    Log.d("let see ","$temperatureEntries")
    val chartEntryModel = entryModelOf(temperatureEntries, moistureEntries)
    val itemsList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    //val chartEntryModel = entryModelOf(entriesOf(4f, 12f, 8f, 16f),
    //   entriesOf(12f, 16f, 4f, 12f))
    // Update the stored value when a new value is fetched

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Chart(
            chart = lineChart(spacing = 20.dp),
            model = chartEntryModel,
           // startAxis = startAxis(),
           // bottomAxis = bottomAxis(),
           // endAxis = endAxis(),
            //marker = Marker,
            isZoomEnabled = true,
          //  fadingEdges = FadingEdges.On(0.5f)
            modifier= Modifier
                .padding(16.dp) // Add some padding to the chart area

        )
    }
}
package com.example.smartestsoil.ui.screens.authentication

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun TemperatureScale(
    minTemperature: Int = 0,
    maxTemperature: Int = 50,
    plantMinTemperature: Int = 20,
    plantMaxTemperature: Int = 30,
    sensorTemperature: Int = 25,
    modifier: Modifier = Modifier,
    scaleHeight: Float = 20f, // New parameter for controlling the height of the scale
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val gradientColors = listOf(
            Color(0xFF66CCFF),
            Color(0xFFBFEFFF),
            Color(0xFFFF6666),
            Color(0xFFFF3333)
        )
        val gradientBrush = Brush.horizontalGradient(
            0f to gradientColors[0],
            0.5f to gradientColors[1],
            0.5f to gradientColors[2],
            1f to gradientColors[3],
            startX = 0f,
            endX = canvasWidth
        )
        drawRect(
            brush = gradientBrush,
            topLeft = Offset(0f, (canvasHeight - scaleHeight) / 2), // Center the scale vertically
            size = Size(canvasWidth, scaleHeight)
        )

        val markerRadius = 10f
        val markerOffset = markerRadius / 2

        // Draw plant minimum temperature marker
        drawCircle(
            color = Color.White,
            radius = markerRadius,
            center = Offset(canvasWidth * (plantMinTemperature.toFloat() / maxTemperature.toFloat()), canvasHeight / 2)
        )
        drawCircle(
            color = gradientColors[0],
            radius = markerRadius - 2f,
            center = Offset(canvasWidth * (plantMinTemperature.toFloat() / maxTemperature.toFloat()), canvasHeight / 2)
        )

        // Draw plant maximum temperature marker
        drawCircle(
            color = Color.White,
            radius = markerRadius,
            center = Offset(canvasWidth * (plantMaxTemperature.toFloat() / maxTemperature.toFloat()), canvasHeight / 2)
        )
        drawCircle(
            color = gradientColors[0],
            radius = markerRadius - 2f,
            center = Offset(canvasWidth * (plantMaxTemperature.toFloat() / maxTemperature.toFloat()), canvasHeight / 2)
        )

        // Draw sensor temperature indicator
        drawLine(
            color = Color.White,
            strokeWidth = 2f,
            start = Offset(canvasWidth * (sensorTemperature.toFloat() / maxTemperature.toFloat()), 0f),
            end = Offset(canvasWidth * (sensorTemperature.toFloat() / maxTemperature.toFloat()), canvasHeight)
        )
        drawCircle(
            color = gradientColors[2],
            radius = markerRadius,
            center = Offset(canvasWidth * (sensorTemperature.toFloat() / maxTemperature.toFloat()), canvasHeight / 2)
        )
    }
}
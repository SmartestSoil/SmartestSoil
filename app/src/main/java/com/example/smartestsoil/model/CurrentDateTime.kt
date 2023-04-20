package com.example.smartestsoil.model

import androidx.compose.runtime.Composable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CurrentDateTime(): String {
    // Create a formatter to format the date and time
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm")

    // Get the current date and time
    val currentDateTime = LocalDateTime.now()

    // Format the current date and time as a string using the formatter

    return currentDateTime.format(formatter)
}
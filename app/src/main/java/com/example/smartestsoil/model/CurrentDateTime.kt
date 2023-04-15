package com.example.smartestsoil.model

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CurrentDateTime(): String {
    // Create a formatter to format the date and time
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm")

    // Get the current date and time
    val currentDateTime = LocalDateTime.now()

    // Format the current date and time as a string using the formatter
    val formattedDateTime = currentDateTime.format(formatter)

    // Display the formatted date and time in a Text composable
    Text(
        text = formattedDateTime,
        modifier = Modifier.padding(10.dp),
        color = MaterialTheme.colors.primary
    )
    return formattedDateTime
}
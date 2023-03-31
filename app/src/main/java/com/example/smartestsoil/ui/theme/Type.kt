package com.example.smartestsoil.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = FontFamily.SansSerif,
    body1 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        color = Color.White
    ),
    button = TextStyle(
        fontSize = 18.sp,
    ),
        /*
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
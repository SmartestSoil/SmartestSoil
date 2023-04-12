package com.example.smartestsoil.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Green02,
    primaryVariant = Green01,
    secondary = Green04,
    secondaryVariant = Green03,
    background = Green02,
    surface = Color.White,
    onPrimary = Color.DarkGray,
    onSecondary = Color.White,
    onBackground = Color.DarkGray,
    onSurface = Color.White,

)

private val LightColorPalette = lightColors(

    primary = Green01,
    primaryVariant = Green02,
    secondary = Green03,
    secondaryVariant = Green04,
    background = Green01,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.Black,

)

@Composable
fun SmartestSoilTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
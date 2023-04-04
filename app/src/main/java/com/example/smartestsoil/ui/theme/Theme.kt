package com.example.smartestsoil.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.smartestsoil.ui.screens.LightDarkToggle

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
    val (isDark, setIsDark) = remember { mutableStateOf(darkTheme) }

    LightDarkToggle(
        modifier = Modifier.padding(vertical = 16.dp),
        onThemeChanged = setIsDark
    )

    val colors = if (isDark) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
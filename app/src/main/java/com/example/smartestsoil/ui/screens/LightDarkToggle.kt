package com.example.smartestsoil.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartestsoil.ui.theme.*

@Composable
fun LightDarkToggle(
    modifier: Modifier = Modifier,
    onThemeChanged: (Boolean) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val (isChecked, setChecked) = remember { mutableStateOf(isDarkTheme) }

    Row(modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = "Light",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = {
                setChecked(it)
                onThemeChanged(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = if (isChecked) Green01 else Green03,
                uncheckedThumbColor = if (isChecked) Green03 else Green01
            ),
            modifier = modifier.padding(start = 8.dp)
        )
        Text(
            text = "Dark",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.weight(1f)
        )
    }
}

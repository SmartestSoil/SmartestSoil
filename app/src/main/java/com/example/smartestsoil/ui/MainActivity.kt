package com.example.smartestsoil.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController

import com.example.smartestsoil.ui.navigation.BottomNav
import com.example.smartestsoil.ui.navigation.NavContro
import com.example.smartestsoil.ui.navigation.TopBar
import com.example.smartestsoil.ui.theme.SmartestSoilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartestSoilTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navContoller = rememberNavController( )

    Scaffold (
        topBar = { TopBar(navContoller) },
        content = { NavContro(navController = navContoller) },
        bottomBar = { BottomNav( navContoller) }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SmartestSoilTheme {
        MainScreen()
    }
}
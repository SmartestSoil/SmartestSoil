package com.example.smartestsoil.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.smartestsoil.ui.navigation.BottomNav
import com.example.smartestsoil.ui.navigation.NavGraph
import com.example.smartestsoil.ui.navigation.TopBar
import com.example.smartestsoil.ui.screens.authentication.Authentication
import com.example.smartestsoil.ui.theme.SmartestSoilTheme
import com.example.smartestsoil.viewModel.FirebaseAuthViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartestSoilTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.onPrimary
                ) {
                    MainScreen()
                    // With this you can access the Authentication
                    //Authentication()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopBar(navController) },
        content = { NavGraph(navController =navController ) },
        bottomBar = { BottomNav(navController) }
    )


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SmartestSoilTheme {
        MainScreen()
    }
}
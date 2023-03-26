package com.example.smartestsoil.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartestsoil.ui.screens.*
import com.example.smartestsoil.ui.screens.authentication.Authentication
import com.example.smartestsoil.viewModel.FirebaseAuthViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun NavController(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination ){
        composable(route = "Home"){
            Home()
        }
        composable(route = "Authentication"){
            Authentication()
        }
        composable(route = "Locations"){
            Locations()
        }
        composable(route = "Details"){
            Details()
        }
    }
}
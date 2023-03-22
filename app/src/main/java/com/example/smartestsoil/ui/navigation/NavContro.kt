package com.example.smartestsoil.ui.navigation

import android.provider.CallLog.Locations
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartestsoil.ui.screens.Details
import com.example.smartestsoil.ui.screens.Home
import com.example.smartestsoil.ui.screens.Locations

@Composable
fun NavContro(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination ="Home" ){
        composable(route = "Home"){
            Home()
        }
        composable(route = "Locations"){
            Locations()
        }
        composable(route = "Details"){
            Details()
        }

    }
}
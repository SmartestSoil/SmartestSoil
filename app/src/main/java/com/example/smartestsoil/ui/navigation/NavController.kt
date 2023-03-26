package com.example.smartestsoil.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartestsoil.model.Routes
import com.example.smartestsoil.ui.screens.*

@Composable
fun NavController(navController: NavHostController = rememberNavController()) {
    /*NavHost(
        navController = navController,
        startDestination = "Home" ){
        composable(route = "Home"){
            Home()
        }
        composable(route = "Locations"){
            Locations()
        }
        composable(route = "Details"){
            Details()
        }
        composable(route = "Login"){
            Login(
                onNavigateToSignUp = {navController.navigate("SignUp")},
                //onNavigateToHome = {navController.navigate("Home")}
            )
        }
        composable(route = "SignUp"){
            SignUp(
                onNavigateToLogin = {navController.navigate("Login")},
                //onNavigateToHome = {navController.navigate("Home")}
            )
        }
    }*/
    NavHost(navController = navController, startDestination = Routes.Login.route) {

        composable(Routes.Login.route) {
            Login(navController = navController)
        }

        composable(Routes.SignUp.route) {
            SignUp(navController = navController)
        }

        composable(Routes.ForgotPassword.route) { navBackStack ->
            ForgotPassword(navController = navController)
        }
    }
}
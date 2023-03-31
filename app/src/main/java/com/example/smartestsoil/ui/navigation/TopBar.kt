package com.example.smartestsoil.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartestsoil.R
import com.example.smartestsoil.model.TabItem
import com.example.smartestsoil.viewModel.FirebaseAuthViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun TopBar(navController: NavController) {
    val firebaseAuthViewModel: FirebaseAuthViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (currentRoute == null || currentRoute == NavRoute.Authentication.path) {
        return
    }
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("")},
        navigationIcon = {
            IconButton(onClick = {navController.navigate("info") }) {
                Image(painter = painterResource(R.drawable.logo_110x110), contentDescription =  null)
            }
        },
        actions = {
            IconButton(
                onClick = { expanded=!expanded }
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded=false }) {
                DropdownMenuItem(
                    onClick = {navController.navigate("info")},

                    ) {
                    Icon(Icons.Filled.Info,  contentDescription = null)
                }
                DropdownMenuItem(onClick = { navController.navigate("account") }) {
                    Icon(Icons.Filled.AccountBox, contentDescription = null)

                }
                DropdownMenuItem(
                    onClick = {
                        if(Firebase.auth.currentUser != null) {
                            firebaseAuthViewModel.logout()
                            navController.navigate("authentication")
                        } else if (Firebase.auth.currentUser == null) {
                            navController.navigate("authentication")
                        }},

                    ) {
                    Icon(Icons.Filled.Logout,  contentDescription = null)
                }

            }
        }

    )


}
package com.example.smartestsoil.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartestsoil.R
import com.example.smartestsoil.model.TabItem

@Composable
fun TopBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (currentRoute == null || currentRoute == NavRoute.Authentication.path) {
        return
    }
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("")},
        navigationIcon = {
            IconButton(onClick = {navController.navigate("Info") }) {
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
                    onClick = {navController.navigate("Info") },

                    ) {
                    Icon(Icons.Filled.Info,  contentDescription = null)
                }
                DropdownMenuItem(onClick = { navController.navigate("Account") }) {
                    Icon(Icons.Filled.AccountBox, contentDescription = null)

                }
                DropdownMenuItem(
                    onClick = {navController.navigate("Logout") },

                    ) {
                    Icon(Icons.Filled.Logout,  contentDescription = null)
                }

            }
        }

    )


}
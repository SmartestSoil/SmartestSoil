package com.example.smartestsoil.ui.navigation

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.smartestsoil.model.TabItem

@Composable
fun TopBar(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text("SmartSoil")},
        navigationIcon = {
            IconButton(onClick = {navController.navigate("Info") }) {
                Icon(Icons.Filled.Menu, contentDescription =  null)
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

            }
        }

    )


}
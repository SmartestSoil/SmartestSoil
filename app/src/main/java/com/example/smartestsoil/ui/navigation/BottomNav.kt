package com.example.smartestsoil.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartestsoil.model.TabItem
import com.example.smartestsoil.ui.screens.AddPlantImage

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomNav(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (currentRoute == null || currentRoute == NavRoute.Authentication.path) {
        return
    }

    val items = listOf(
        TabItem("Home", Icons.Filled.Home,"home"),
        TabItem("Soil", Icons.Filled.LocationOn,"locations"),
        TabItem("Detail", Icons.Filled.LocationOn,"detail"),
    )
    var selectedItem by remember { mutableStateOf(0) }

    // State to track whether the dialog form is open
    var showDialog by remember { mutableStateOf(false) }

    // Function to handle FAB click and open the dialog form where new plant can be added
    fun onFABClick() {
        showDialog = true
    }
    // Dialog form composable for adding a plant
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            content = {
                AddPlantImage()
                /*Button(
                    onClick = { showDialog = false }
                ) {
                    Text("OK")
                }*/
            }
        )
    }

    Scaffold(
        topBar = { TopBar(navController = navController) },
        bottomBar = {
            BottomAppBar(
                cutoutShape = CircleShape,
                backgroundColor = Color.White
            ) {
                items.forEachIndexed { index, item ->
                    BottomNavigationItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(item.route)
                        },
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primaryVariant
                            ) },
                        label = {
                            Text(
                                text = item.label,
                                color = MaterialTheme.colors.primaryVariant
                            ) }
                    )
                }
            }
        },
        floatingActionButton = {
            OutlinedButton(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                border = BorderStroke(1.5.dp, Color.White),
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = MaterialTheme.colors.primaryVariant,
                    backgroundColor = MaterialTheme.colors.primaryVariant
                ),
                onClick = { onFABClick() }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
    ) {
        // Add some padding to the content to make space for the FAB
        Column(modifier = Modifier.padding(bottom = 56.dp)) {
            // Content here
        }
    }

}
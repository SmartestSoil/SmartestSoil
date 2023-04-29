package com.example.smartestsoil.ui.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartestsoil.model.TabItem

@Composable
fun BottomNav(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (currentRoute == null || currentRoute == NavRoute.Authentication.path || currentRoute == NavRoute.Home.path ) {
        return
    }

    val items = listOf(
        TabItem("News Feed", Icons.Filled.Newspaper,"feeds"),
        TabItem("My plants", Icons.Filled.List,"plantlist"),
        TabItem("Detail", Icons.Filled.LocationOn,"detail"),
    )
    var selectedItem by remember { mutableStateOf(0) }
    BottomNavigation() {
        items.forEachIndexed{index, item ->
            BottomNavigationItem(
                selected = selectedItem== index,
                onClick = {
                    selectedItem =index

                    navController.navigate(item.route)
                },
                icon = {Icon(item.icon, contentDescription = null)},
                label ={ Text(item.label)}
            )
        }
    }

}
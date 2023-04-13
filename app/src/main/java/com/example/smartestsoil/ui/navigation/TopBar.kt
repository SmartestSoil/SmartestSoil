package com.example.smartestsoil.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartestsoil.R
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
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(7.dp),
                onClick = { navController.navigate("home") }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_without_text_300),
                    contentDescription = "Logo",
                )
            }},
        title = {
            Text(
                text = "SmartestSoil",
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Light
            )},
        backgroundColor = Color.White,
        actions = {
            IconButton(
                onClick = { expanded=!expanded }
            ) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primaryVariant
                    )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded=false }) {
                DropdownMenuItem(
                    onClick = {navController.navigate("info")},

                    ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primaryVariant
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "About",
                        color = MaterialTheme.colors.primary
                    )
                }
                DropdownMenuItem(onClick = { navController.navigate("account") }) {
                    Icon(
                        Icons.Filled.AccountCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primaryVariant
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Account",
                        color = MaterialTheme.colors.primary
                    )
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
                    Icon(
                        Icons.Filled.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primaryVariant
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Logout",
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
    )
}
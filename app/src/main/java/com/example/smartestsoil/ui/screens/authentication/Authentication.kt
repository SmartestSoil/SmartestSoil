package com.example.smartestsoil.ui.screens.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.smartestsoil.viewModel.FirebaseAuthViewModel

@Composable
fun Authentication(
    navController: NavHostController
) {
    val viewModel: FirebaseAuthViewModel = viewModel()

    AuthenticationContent(
        modifier = Modifier.fillMaxWidth(),
        authenticationState = viewModel.uiState.collectAsState().value,
        handleEvent = viewModel::handleEvent,
        navController = navController
    )
}
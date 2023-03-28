package com.example.smartestsoil.ui.screens

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.smartestsoil.ui.screens.authentication.AuthenticationButton
import com.example.smartestsoil.viewModel.FirebaseAuthViewModel
import com.google.api.Context

@Composable
fun Home() {
    val firebaseAuthViewModel: FirebaseAuthViewModel = viewModel()
    Text(text = "this is the home")
    Button(
        onClick = { firebaseAuthViewModel.logout() }
    ) {
        Text(text = "Logout")
    }
}
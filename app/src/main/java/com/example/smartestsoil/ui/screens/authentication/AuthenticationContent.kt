package com.example.smartestsoil.ui.screens.authentication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.smartestsoil.model.AuthState
import com.example.smartestsoil.viewModel.AuthEvent

@Composable
fun AuthenticationContent(
    modifier: Modifier = Modifier,
    authenticationState: AuthState,
    handleEvent: (event: AuthEvent) -> Unit,
    navController: NavHostController
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (authenticationState.isLoading) {
            CircularProgressIndicator()
        } else {
            AuthenticationForm(
                modifier = Modifier.fillMaxSize(),
                authenticationMode = authenticationState.authenticationMode,
                email = authenticationState.email,
                password = authenticationState.password,
                completedPasswordRequirements = authenticationState.passwordRequirements,
                enableAuthentication = authenticationState.isFormValid(),
                navController = navController,
                onEmailChanged = {
                    handleEvent(AuthEvent.EmailChanged(it))
                },
                onPasswordChanged = {
                    handleEvent(AuthEvent.PasswordChanged(it))
                },
                onAuthenticate = {
                    handleEvent(AuthEvent.Authenticate)
                },
                onToggleMode = {
                    handleEvent(
                        AuthEvent.ToggleAuthenticationMode)
                },
                authenticationState = authenticationState
            )
            authenticationState.error?.let { error ->
                AuthenticationErrorDialog(
                    error = error,
                    dismissError = {
                        handleEvent(
                            AuthEvent.ErrorDismissed)
                    }
                )
            }
        }
    }
}
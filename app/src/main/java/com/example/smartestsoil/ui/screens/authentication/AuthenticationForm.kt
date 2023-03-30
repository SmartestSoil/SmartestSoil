package com.example.smartestsoil.ui.screens.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.smartestsoil.model.AuthenticationMode
import com.example.smartestsoil.model.PasswordRequirements

@Composable
fun AuthenticationForm(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode,
    email: String,
    password: String,
    completedPasswordRequirements: List<PasswordRequirements>,
    enableAuthentication: Boolean,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onAuthenticate: () -> Unit,
    onToggleMode: () -> Unit,
    navController: NavHostController
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        AuthenticationTitle(
            modifier = Modifier.fillMaxWidth(),
            authenticationMode = authenticationMode
        )
        Spacer(modifier = Modifier.height(40.dp))
        val passwordFocusRequester = FocusRequester()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailInput(
                modifier = Modifier.fillMaxWidth(),
                email = email,
                onEmailChanged = onEmailChanged
            ) {
                passwordFocusRequester.requestFocus()
            }
            Spacer(modifier = Modifier.height(16.dp))
            PasswordInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester),
                password = password,
                onPasswordChanged = onPasswordChanged,
                onDoneClicked = onAuthenticate
            )
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedVisibility(
                visible = authenticationMode ==
                        AuthenticationMode.SIGN_UP
            ) {
                //PasswordRequirements(completedPasswordRequirements)
                PasswordRequirements(satisfiedRequirements = completedPasswordRequirements)
            }
            Spacer(modifier = Modifier.height(12.dp))
            AuthenticationButton(
                enableAuthentication = enableAuthentication,
                authenticationMode = authenticationMode,
                onAuthenticate = onAuthenticate,
                navController = navController
            )
            Spacer(modifier = Modifier.weight(1f))
            ToggleAuthenticationMode(
                modifier = Modifier.fillMaxWidth(),
                authenticationMode = authenticationMode,
                toggleAuthentication = {
                    onToggleMode()
                }
            )
        }
    }
}

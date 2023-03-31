package com.example.smartestsoil.ui.screens.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.smartestsoil.R
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.smartestsoil.model.AuthState
import com.example.smartestsoil.model.AuthenticationMode
import com.example.smartestsoil.model.PasswordRequirements

@OptIn(ExperimentalComposeUiApi::class)
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
    navController: NavHostController,
    authenticationState: AuthState,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .scrollable(state = scrollState, orientation = Orientation.Vertical)
            .clickable(onClick = {
                keyboardController?.hide()
            }),
        ) {
        Spacer(modifier = Modifier.height(80.dp))
        Image(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.logo_round_white_300),
            contentDescription = "Logo",
            )
        Spacer(modifier = Modifier.height(80.dp))
        val passwordFocusRequester = FocusRequester()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
        ) {
            AuthenticationTitle(
                modifier = Modifier.fillMaxWidth(),
                authenticationMode = authenticationMode
            )
            ToggleAuthenticationMode(
                modifier = Modifier.fillMaxWidth(),
                authenticationMode = authenticationMode,
                toggleAuthentication = { onToggleMode() }
            )
            Spacer(modifier = Modifier.height(10.dp))
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
                onDoneClicked =  {
                    keyboardController?.hide()
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedVisibility(
                visible = authenticationMode ==
                        AuthenticationMode.SIGN_UP
            ) {
                PasswordRequirements(satisfiedRequirements = completedPasswordRequirements)
            }
            Spacer(modifier = Modifier.height(30.dp))
            AuthenticationButton(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                enableAuthentication = enableAuthentication,
                authenticationMode = authenticationMode,
                onAuthenticate = onAuthenticate,
                navController = navController,
                authenticationState = authenticationState
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

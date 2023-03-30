package com.example.smartestsoil.ui.screens.authentication

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.smartestsoil.R
import com.example.smartestsoil.model.AuthState
import com.example.smartestsoil.model.AuthenticationMode

@Composable
fun AuthenticationButton(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode,
    enableAuthentication: Boolean,
    onAuthenticate: () -> Unit,
    navController: NavHostController,
    authenticationState: AuthState,
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = MaterialTheme.colors.primaryVariant,
            backgroundColor = MaterialTheme.colors.primaryVariant
        ),
        onClick = {
            onAuthenticate()
            if (enableAuthentication) {
                navController.navigate("Home")
            } else {
                authenticationState.error
            }
        },
        enabled = enableAuthentication
    ) {
        Text(
            modifier = modifier.padding(5.dp),
            text = stringResource(
                if (authenticationMode ==
                    AuthenticationMode.SIGN_IN) {
                    R.string.action_sign_in
                } else {
                    R.string.action_sign_up
                }
            ),
            color = MaterialTheme.colors.onPrimary
        )
    }
}
package com.example.smartestsoil.ui.screens.authentication

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.smartestsoil.R
import com.example.smartestsoil.model.AuthenticationMode
import com.example.smartestsoil.viewModel.AuthEvent

@Composable
fun AuthenticationButton(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode,
    enableAuthentication: Boolean,
    onAuthenticate: () -> Unit,
    navController: NavHostController

) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(1.dp),
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = MaterialTheme.colors.primary
        ),
        onClick = {
            onAuthenticate()
            if (enableAuthentication) {
                navController.navigate("Home")
            } else {
                Log.d("ERRROOOOOOOR", "HELP")
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
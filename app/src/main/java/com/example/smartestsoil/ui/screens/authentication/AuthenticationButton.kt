package com.example.smartestsoil.ui.screens.authentication

import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.smartestsoil.R
import com.example.smartestsoil.model.AuthenticationMode

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
        onClick = {
            onAuthenticate()
            if (enableAuthentication){
                navController.navigate("home")

            }else{
                Log.d("ERRRRRROOOOOR","HELP")
            }
        },



    ) {
        Text(
            text = stringResource(
                if (authenticationMode ==
                    AuthenticationMode.SIGN_IN) {
                    R.string.action_sign_in
                } else {
                    R.string.action_sign_up
                }
            )
        )
    }
}
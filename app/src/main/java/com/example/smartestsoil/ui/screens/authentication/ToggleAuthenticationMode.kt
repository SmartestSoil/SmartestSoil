package com.example.smartestsoil.ui.screens.authentication

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.smartestsoil.R
import com.example.smartestsoil.model.AuthenticationMode

@Composable
fun ToggleAuthenticationMode(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode,
    toggleAuthentication: () -> Unit
) {
        TextButton(
            onClick = { toggleAuthentication() },
        ) {
            Text(
                text = (
                if (authenticationMode ==
                    AuthenticationMode.SIGN_IN) {
                    buildAnnotatedString {
                        append(stringResource(R.string.action_no_account))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.action_sign_up))
                        }
                    }
                } else {
                    buildAnnotatedString {
                        append(stringResource(R.string.action_already_have_account))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.action_sign_in))
                        }
                    }
                }
                )
            )
        }
}

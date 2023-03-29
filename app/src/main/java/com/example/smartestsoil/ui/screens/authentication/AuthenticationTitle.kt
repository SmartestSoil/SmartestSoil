package com.example.smartestsoil.ui.screens.authentication

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.smartestsoil.R
import com.example.smartestsoil.model.AuthenticationMode
import com.example.smartestsoil.ui.theme.SmartestSoilTheme

@Composable
fun AuthenticationTitle(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode
) {
    Text(
        text = stringResource(
            if (authenticationMode == AuthenticationMode.SIGN_IN) {
                R.string.label_sign_in
            } else {
                R.string.label_sign_up
            }
        ),
        color = MaterialTheme.colors.onPrimary,
        fontSize = 30.sp,
        modifier = Modifier.padding(horizontal = Dp(7F))
    )
}
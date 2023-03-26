package com.example.smartestsoil.viewModel

sealed class AuthEvent {
    object ToggleAuthenticationMode: AuthEvent()

    class EmailChanged(val email: String): AuthEvent()

    class PasswordChanged(val password: String): AuthEvent()

    object Authenticate: AuthEvent()

    object ErrorDismissed: AuthEvent()

}

package com.example.smartestsoil.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartestsoil.model.AuthState
import com.example.smartestsoil.model.AuthenticationMode
import com.example.smartestsoil.model.PasswordRequirements
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseAuthViewModel: ViewModel() {
    val uiState = MutableStateFlow(AuthState())
    var user = mutableStateOf<FirebaseUser?>(null)
    var startDestination = mutableStateOf("")

    private fun toggleAuthenticationMode() {
        val authenticationMode = uiState.value.authenticationMode
        val newAuthenticationMode = if (
            authenticationMode == AuthenticationMode.SIGN_IN
        ) {
            AuthenticationMode.SIGN_UP
        } else {
            AuthenticationMode.SIGN_IN
        }
        uiState.value = uiState.value.copy(
            authenticationMode = newAuthenticationMode
        )
    }

    fun handleEvent(authenticationEvent: AuthEvent) {
        when (authenticationEvent) {
            is AuthEvent.ToggleAuthenticationMode -> {
                toggleAuthenticationMode()
            }
            is AuthEvent.EmailChanged -> {
                updateEmail(authenticationEvent.email)
            }
            is AuthEvent.PasswordChanged -> {
                updatePassword(authenticationEvent.password)
            }
            is AuthEvent.Authenticate -> {
                authenticate(uiState.value.email, uiState.value.password)
            }
            is AuthEvent.ErrorDismissed -> {
                dismissError()
            }
        }
    }

    private fun updateEmail(email: String) {
        uiState.value = uiState.value.copy(
            email = email
        )
    }

    private fun updatePassword(password: String) {
        val requirements = mutableListOf<PasswordRequirements>()
        if (password.length > 7) {
            requirements.add(PasswordRequirements.EIGHT_CHARACTERS)
        }
        if (password.any { it.isUpperCase() }) {
            requirements.add(PasswordRequirements.CAPITAL_LETTER)
        }
        if (password.any { it.isDigit() }) {
            requirements.add(PasswordRequirements.NUMBER)
        }
        uiState.value = uiState.value.copy(
            password = password,
            passwordRequirements = requirements.toList()
        )
    }

    private fun dismissError() {
        uiState.value = uiState.value.copy(
            error = null
        )
    }

    private fun authenticate(email: String, password: String) {
        val authenticationMode = uiState.value.authenticationMode

        uiState.value = uiState.value.copy(
            isLoading = true
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val authResult = if (authenticationMode == AuthenticationMode.SIGN_IN) {
                    Firebase.auth.signInWithEmailAndPassword(email, password).await()
                } else {
                    Firebase.auth.createUserWithEmailAndPassword(email, password).await()
                }
                withContext(Dispatchers.Main) {
                    user.value = authResult.user
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                    // Here I tried to update the startDestination to be Home and then the
                    startDestination.value = "Home"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    /*fun signUp(email: String, password: String) {
        viewModelScope.launch {
            Firebase.auth
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    user.value = it.user
                    Log.d("*****", "Registration done!!")
                }
                .addOnFailureListener {
                    Log.e("*****", it.message.toString())
                }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            Firebase.auth
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    user.value = it.user
                    Log.d("*****", "Sign in done!!")
                }
                .addOnFailureListener {
                    Log.e("*****", it.message.toString())
                }
        }
    }*/

    fun logout() {
        viewModelScope.launch {
            Firebase.auth.signOut()
            startDestination.value = "Authenticate"
            user.value = null
        }
    }
}

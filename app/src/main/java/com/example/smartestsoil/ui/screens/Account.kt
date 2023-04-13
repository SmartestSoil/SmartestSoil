package com.example.smartestsoil.ui.screens

import android.accounts.Account
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun Account(navController: NavController, userEmail: String) {

    val user = FirebaseAuth.getInstance().currentUser

    // Remember the number of sensors as a state
    var numOfSensors by remember { mutableStateOf(0) }


    // Retrieve the number of sensors from Firestore when the composable is first created
    LaunchedEffect(key1 = "numOfSensors") {
        val db = Firebase.firestore
        val sensorsCollection = db.collection("sensors")
        sensorsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                numOfSensors = querySnapshot.size()
                // Update the state with the retrieved number of sensors

            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Welcome, ${user?.email}",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "You have $numOfSensors plants.",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Add more content to the screen as needed
    }
}

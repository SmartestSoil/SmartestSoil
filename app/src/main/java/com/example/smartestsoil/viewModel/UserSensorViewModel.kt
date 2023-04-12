package com.example.smartestsoil.viewModel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayInputStream
import java.util.*

class UserSensorViewModel: ViewModel() {
    var sensors = mutableStateListOf<String>()
        private set

    private val storageRef = Firebase.storage.reference

    fun addSensor(sensorname: String, imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            // Create a reference to the image file in Firebase Storage
            val imageRef = storageRef.child("images/${UUID.randomUUID()}")

            // Convert the ByteArray to an InputStream
            //val inputStream = ByteArrayInputStream(imageData)

            // Upload the image using putStream() instead of putFile()
            //val uploadTask = imageRef.putStream(inputStream)
            val uploadTask = imageRef.putFile(imageUri)

            // Wait for the upload to complete and get the download URL of the image
            val downloadUrl = uploadTask.await().storage.downloadUrl.await().toString()

            // Store the sensor name and image download URL to Firestore
            Firebase.firestore.collection("sensors")
                .document(sensorname)
                .set(hashMapOf("imageUri" to downloadUrl))
                .addOnCompleteListener {  }
                .addOnFailureListener { }
        }
    }

    fun getSensor() {
        viewModelScope.launch {
            Firebase.firestore.collection("sensors")
                .get()
                .addOnSuccessListener {
                    val imageUri = mutableListOf<String>()
                    it.documents.forEach{ doc ->
                        imageUri.add(doc.get("imageUri").toString())
                    }
                    sensors.clear()
                    sensors.addAll(imageUri)
                }
                .addOnFailureListener {  }
        }
    }
}
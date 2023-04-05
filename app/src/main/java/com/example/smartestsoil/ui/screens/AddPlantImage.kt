package com.example.smartestsoil.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import com.example.smartestsoil.R
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.smartestsoil.model.addUserSensor
import com.example.smartestsoil.ui.screens.authentication.EmailInput
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.patrykandpatrick.vico.core.extension.setFieldValue
import java.util.*

@Composable
fun AddPlantImage() {
    var imageUri by remember { mutableStateOf<android.net.Uri?>(null) }

    var sensorName by remember { mutableStateOf("") }

    val context = LocalContext.current
    val storageRef = FirebaseStorage.getInstance().reference
    val userId = Firebase.auth.currentUser?.uid

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null && isImage(uri, context)) {
                imageUri = uri
            } else {
                Toast.makeText(context, "Please select an image file", Toast.LENGTH_SHORT).show()
            }
        }

    Surface(
        shape = RoundedCornerShape(1.dp),
        color = Color.White
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Add a plant",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier.size(150.dp)
                        .clip(RoundedCornerShape(75.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberImagePainter(data = imageUri),
                            contentDescription = "Selected Image",
                            modifier = Modifier.size(140.dp)
                                .clip(RoundedCornerShape(70.dp))
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.logo_round_white_300),
                            contentDescription = "Placeholder Image",
                            modifier = Modifier.size(140.dp)
                                .clip(RoundedCornerShape(70.dp))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = sensorName,
                    onValueChange = { sensorName = it },
                    label = { Text(text = "Sensor name") },
                    placeholder = { Text(text = "Enter a name for your sensor") },
                    singleLine = true,
                    shape = RoundedCornerShape(1.dp),
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { addUserSensor(sensorName) },
                    colors = ButtonDefaults.buttonColors(
                        disabledBackgroundColor = MaterialTheme.colors.primary,
                        backgroundColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = "Save",
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { launcher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(
                        disabledBackgroundColor = MaterialTheme.colors.primary,
                        backgroundColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = "Select Image",
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (imageUri != null) {
                            uploadImage(imageUri!!, storageRef, context)
                        } else {
                            Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        disabledBackgroundColor = MaterialTheme.colors.primary,
                        backgroundColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = "Upload Image",
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    }
}

private fun uploadImage(uri: android.net.Uri, storageRef: StorageReference, context: Context) {
    val imageRef = storageRef.child("images/${UUID.randomUUID()}")
    imageRef.putFile(uri).addOnSuccessListener {
        imageRef.downloadUrl.addOnSuccessListener { url ->
            Log.d("***", url.toString())
            Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener{
                Log.e("***", it.message.toString())
            }
    }
}

private fun isImage(uri: android.net.Uri, context: Context): Boolean {
    val contentTypeResolver = context.contentResolver
    val type = contentTypeResolver.getType(uri)
    return type?.startsWith("image/") == true
}
package com.example.smartestsoil.ui.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import com.example.smartestsoil.R
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddSensor(onClose: () -> Unit) {
    var imageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var sensorname by remember { mutableStateOf("") }

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
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 5.dp)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { onClose() }, modifier = Modifier.align(Alignment.End)) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colors.primary
                )
            }
            Text(
                text = "Add Sensor",
                color = MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier.size(124.dp)
                    .clip(RoundedCornerShape(70.dp))
                    .background(MaterialTheme.colors.primary),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberImagePainter(data = imageUri),
                        contentDescription = "Selected Image",
                        modifier = Modifier.size(120.dp)
                            .clip(RoundedCornerShape(70.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.logo_without_text_300),
                        contentDescription = "Placeholder Image",
                        modifier = Modifier.size(120.dp)
                            .clip(RoundedCornerShape(70.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.secondary)
                        .clickable(onClick = { /* Handle image button click */ })
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Image",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Give a name for your sensor:",
                color = MaterialTheme.colors.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(
                value = sensorname,
                onValueChange = { sensorname = it },
                label = { Text(
                    text = "Sensor name",
                    color = MaterialTheme.colors.primaryVariant
                ) },
                placeholder = { Text(text = "Enter a name for your sensor") },
                singleLine = true,
                shape = RoundedCornerShape(1.dp),
                textStyle =
                    TextStyle(
                        color = MaterialTheme.colors.primary
                    )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { addUserSensor(sensorname, imageUri!!) },
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
                    }},
                colors =
                    ButtonDefaults.buttonColors(
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

val storage = Firebase.storage
val storageRef = storage.reference.child("sensorData")

data class UserSensor(
    var sensorname: String,
    var imageUri: Uri?
)

fun addUserSensor(sensorname: String, imageUri: Uri) {
    val userId = Firebase.auth.currentUser!!.uid
    val database = Firebase.database.reference
    val userSensor = UserSensor(sensorname, imageUri)

    // Upload the image to Cloud Storage
    val imageRef = storageRef.child("$userId/$sensorname.jpg")
    val uploadTask = imageRef.putFile(imageUri)
    uploadTask.addOnSuccessListener {
        // Get the download URL of the uploaded image
        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
            // Save the sensor data (including the image URL) to the Realtime Database
            userSensor.imageUri = downloadUri
            database.child("sensorData").child(userId).setValue(userSensor)
        }
    }
}
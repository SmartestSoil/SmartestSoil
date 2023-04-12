package com.example.smartestsoil.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import com.example.smartestsoil.R
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartestsoil.model.BottomSheetItem
import com.example.smartestsoil.viewModel.UserSensorViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AddSensor(onClose: () -> Unit) {
    var imageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    //var imageData by remember { mutableStateOf<ByteArray?>(null) }
    var sensorname by remember { mutableStateOf("") }
    //val userSensorViewModel: UserSensorViewModel = viewModel()

    val context = LocalContext.current
    val userId = Firebase.auth.currentUser?.uid
    val currentUser = FirebaseAuth.getInstance().currentUser
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    // Firebase references
    val auth = Firebase.auth
    val storageRef = FirebaseStorage.getInstance().reference
    val firestoreDb = Firebase.firestore

    // Function to upload image to Firebase Storage and store sensor data to Firestore
    fun addSensor() {
        // Check if user is authenticated
        val user = auth.currentUser
        if (user == null) {
            // User is not authenticated, show error message
            Toast.makeText(context, "You must be logged in to add a sensor", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if sensor name is empty
        if (sensorname.isEmpty()) {
            // Sensor name is empty, show error message
            Toast.makeText(context, "Please enter a sensor name", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if image is selected
        if (imageUri == null) {
            // Image is not selected, show error message
            Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a unique filename for the image
        val filename = "${UUID.randomUUID()}.jpg"

        // Upload image to Firebase Storage
        val imagesRef = storageRef.child("images/$filename")
        imagesRef.putFile(imageUri!!)
            .addOnSuccessListener {
                // Image upload successful, get the download URL
                imagesRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    // Store sensor data to Firestore
                    val sensorData = hashMapOf(
                        "sensorName" to sensorname,
                        "imageUrl" to downloadUrl.toString()
                    )
                    firestoreDb.collection("sensors")
                        .document(sensorname)
                        .set(sensorData)
                        .addOnSuccessListener {
                            // Sensor data stored successfully, show success message
                            Toast.makeText(context, "Sensor added successfully", Toast.LENGTH_SHORT).show()
                            onClose()
                        }
                        .addOnFailureListener { e ->
                            // Sensor data storage failed, show error message
                            Toast.makeText(context, "Error adding sensor: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                // Image upload failed, show error message
                Toast.makeText(context, "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun bitmapToByteArray(bitmap: Bitmap?): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            /*val bitmap = context.contentResolver?.openInputStream(it)?.use { stream -> BitmapFactory.decodeStream(stream) }
            imageData = bitmapToByteArray(bitmap)*/
        }
    }

    // !!!!! do this in the addSensor function instead !!!!!
    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val byteArray = bitmapToByteArray(bitmap)
        val path = "${UUID.randomUUID()}.jpg"
        val imagesRef: StorageReference = storageRef.child("images/$userId/$path")
        imagesRef.putBytes(byteArray)
            .addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    imageUri = uri
                }
            }
        //imageData = byteArray
    }

    fun chooseFromLibrary() {
        launcher.launch("image/*")
    }

    fun takePhoto() {
        cameraLauncher.launch(null)
    }

    fun removeCurrentPicture() {
        imageUri = null
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    val bottomSheetItems = listOf(
        BottomSheetItem(title = "Choose from library", icon = Icons.Outlined.PhotoLibrary, onClick = ::chooseFromLibrary),
        BottomSheetItem(title = "Take photo", icon = Icons.Outlined.PhotoCamera, onClick = ::takePhoto),
        BottomSheetItem(title = "Remove current picture", icon = Icons.Outlined.Delete, onClick = ::removeCurrentPicture)
    )

    Surface(
        shape = RoundedCornerShape(1.dp),
        color = Color.White
    ) {
        BottomSheetScaffold(
            backgroundColor = Color.White,
            scaffoldState = bottomSheetScaffoldState,
            sheetShape = RoundedCornerShape(topEnd = 30.dp, topStart = 30.dp),
            sheetContent = {
                //UI for bottom sheet
                Column(
                    content = {
                        Spacer(modifier = Modifier.padding(16.dp))
                        Text(
                            text = "Add an image of your Sensor/Plant",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 21.sp,
                            color = MaterialTheme.colors.onPrimary
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(1),
                        ){
                            items(bottomSheetItems.size, itemContent = {
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 24.dp)
                                        .clickable { bottomSheetItems[it].onClick() },
                                ) {
                                    Spacer(modifier = Modifier.padding(8.dp))
                                    Icon(
                                        bottomSheetItems[it].icon,
                                        bottomSheetItems[it].title,
                                        tint = MaterialTheme.colors.onPrimary
                                    )
                                    Spacer(modifier = Modifier.padding(8.dp))
                                    Text(text = bottomSheetItems[it].title, color = MaterialTheme.colors.onPrimary)
                                }
                            })
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .background(brush = Brush.linearGradient(colors = listOf(MaterialTheme.colors.primary, MaterialTheme.colors.secondary)),)
                        .padding(16.dp),

                    )
            },
            sheetPeekHeight = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 5.dp)
                    .verticalScroll(state = rememberScrollState())
                    .clickable(onClick = {
                        keyboardController?.hide()
                    }),
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
                            .clickable(
                                onClick = {
                                    coroutineScope.launch {
                                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        } else {
                                            bottomSheetScaffoldState.bottomSheetState.collapse()
                                        }
                                    }
                                })
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
                    label = {
                        Text(
                            text = "Sensor name",
                            color = MaterialTheme.colors.primaryVariant
                        )
                    },
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
                    onClick = {
                        if (sensorname.isNotEmpty() && imageUri != null) {
                            if (currentUser != null) {
                                val userId = currentUser.uid
                                val storageRef = FirebaseStorage.getInstance().reference
                                currentUser.getIdToken(true)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val token = task.result?.token
                                            // Do something with the token
                                        } else {
                                            // Show an error toast
                                            Toast.makeText(context, "Failed to get token: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(context, "User is not signed in", Toast.LENGTH_SHORT).show()
                            }
                            addSensor()
                            onClose()
                        } else {
                            Toast.makeText(
                                context,
                                "Please provide a name and an image for your sensor",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
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
                            //uploadImage(imageUri!!, storageRef, context)
                        } else {
                            Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
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
}
/*
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
}*/
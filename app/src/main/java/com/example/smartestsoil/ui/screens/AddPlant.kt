package com.example.smartestsoil.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.zIndex
import com.example.smartestsoil.model.BottomSheetItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AddPlant(onClose: () -> Unit) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val defaultImageUri = Uri.parse("android.resource://com.example.smartestsoil/drawable/logo_without_text_300")
    var pairedSensor by remember { mutableStateOf("") }
    var plantId =UUID.randomUUID().toString()
    var plantName by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userId = Firebase.auth.currentUser?.uid
    val currentUser = FirebaseAuth.getInstance().currentUser
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    // Firebase references
    val auth = Firebase.auth
    val storageRef = Firebase.storage.reference
    val firestoreDb = Firebase.firestore

    // Function to upload image to Firebase Storage and store plant data to Firestore
    fun addPlant() {
        // Check if user is authenticated
        val user = auth.currentUser
        if (user == null) {
            // User is not authenticated, show error message
            Toast.makeText(context, "You must be logged in to add a plant", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Check if sensor name is empty
        if (plantName.isEmpty()) {
            // Sensor name is empty, show error message
            Toast.makeText(context, "Please enter a plant name", Toast.LENGTH_SHORT).show()
            return
        }

        // If user did not select or take an image, set it to default
        if (imageUri == null) {
            imageUri = defaultImageUri
        }

        // Create a unique filename for the image
        val filename = "${UUID.randomUUID()}.jpg"

        // Upload image to Firebase Storage
        //val storageRef = storageRef.child("$filename")
        //imageUri?.let { u ->
          //  storageRef.putFile(u)
           //     .addOnSuccessListener { remoteUri ->
             //       Log.d("*****", remoteUri.toString())
             //       storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        // Store plant data to Firestore
                if(imageUri != null){
                        val plantData = hashMapOf(
                            "imageUrl" to imageUri.toString(),
                            "pairedSensor" to pairedSensor,
                            "plantId" to plantId,
                            "plantName" to plantName,
                            "userId" to userId

                        )

                        firestoreDb.collection("plants")
                            .document(plantId)
                            .set(plantData)
                            .addOnSuccessListener {
                                // Plant data stored successfully, show success message
                                Toast.makeText(context, "Plant added successfully", Toast.LENGTH_SHORT).show()
                                onClose()
                            }
                            .addOnFailureListener { e ->
                                // Plant data storage failed, show error message
                                Toast.makeText(context, "Error adding sensor: ${e.message}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
             //   }
            //    .addOnFailureListener { e ->
            //        Log.e("ERROR*****", e.message.toString())
            //    }
    //    }
    }

    fun bitmapToByteArray(bitmap: Bitmap?): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
        }
    }

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

    fun chooseFromLibrary() {
        launcher.launch("image/*")
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    val bottomSheetItems = listOf(
        BottomSheetItem(title = "Choose from library", icon = Icons.Outlined.PhotoLibrary, onClick = ::chooseFromLibrary, ),
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
                            text = "Add an image of your Plant",
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
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colors.primary,
                                    MaterialTheme.colors.secondary
                                )
                            ),
                        )
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
                        tint = MaterialTheme.colors.primary,
                        modifier= Modifier
                            .padding(12.dp)
                            .zIndex(1f),
                    )
                }
                Text(
                    text = "Add Plant",
                    color = MaterialTheme.colors.primaryVariant,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(124.dp)
                        .clip(RoundedCornerShape(70.dp))
                        .background(MaterialTheme.colors.primary)
                        .zIndex(0f),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberImagePainter(data = imageUri),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(70.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.logo_without_text_300),
                            contentDescription = "Placeholder Image",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(70.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Box(modifier = Modifier
                    .offset(y = (-35).dp, x = (40).dp)
                    .zIndex(1f),
                    contentAlignment = Alignment.Center) {
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
                                }),
                        contentAlignment = Alignment.Center,

                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Image",
                            tint = MaterialTheme.colors.onPrimary,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "Give a name to your plant:",
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = plantName,
                    onValueChange = { plantName = it },
                    label = {
                        Text(
                            text = "Plant name",
                            color = MaterialTheme.colors.primaryVariant
                        )
                    },
                    placeholder = { Text(text = "Enter a name for your plant") },
                    singleLine = true,
                    shape = RoundedCornerShape(1.dp),
                    textStyle =
                    TextStyle(
                        color = MaterialTheme.colors.primary
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Add a sensor to your plant:",
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = pairedSensor,
                    onValueChange = { pairedSensor = it },
                    label = {
                        Text(
                            text = "Paired sensor",
                            color = MaterialTheme.colors.primaryVariant
                        )
                    },
                    placeholder = { Text(text = "Add the sensor name") },
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
                        addPlant()
                        onClose()
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
            }
        }
    }
}
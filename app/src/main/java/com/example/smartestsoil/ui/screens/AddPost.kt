package com.example.smartestsoil.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.smartestsoil.R
import com.example.smartestsoil.model.BottomSheetItem
import com.example.smartestsoil.model.UserPost
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.Date

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AddPost(onClose: () -> Unit) {
    val user = FirebaseAuth.getInstance().currentUser?.displayName ?: ""
    var postContent by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val userId = Firebase.auth.currentUser?.uid
    val currentUser = FirebaseAuth.getInstance().currentUser
    val keyboardController = LocalSoftwareKeyboardController.current

    val auth = Firebase.auth
    val storageRef = Firebase.storage.reference
    val firestoreDb = Firebase.firestore

    // Function to upload image to Firebase Storage and store plant data to Firestore
    fun addPost() {
        // Check if user is authenticated
        val date: Date = Timestamp.now().toDate()
        val user = auth.currentUser
        if (user == null) {
            // User is not authenticated, show error message
            Toast.makeText(context, "You must be logged share post", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Check if sensor name is empty
        if (postContent.isEmpty()) {
            // Sensor name is empty, show error message
            Toast.makeText(context, "create post by adding your thoughts", Toast.LENGTH_SHORT).show()
            return
        }



        // Create a unique filename for the image
        val filename = "${UUID.randomUUID()}.jpg"

        // Upload image to Firebase Storage
        val storageRef = storageRef.child("$filename")
        imageUri?.let { u ->
            storageRef.putFile(u)
                .addOnSuccessListener { remoteUri ->
                    Log.d("*****", remoteUri.toString())
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val postData = hashMapOf(
                            "media" to downloadUrl.toString(),
                            "body" to postContent,
                            "date" to date,
                            "user" to userId
                        )
                        firestoreDb.collection("posts")
                            .document(date.toString())
                            .set(postData)
                            .addOnSuccessListener {
                                // Plant data stored successfully, show success message
                                Toast.makeText(
                                    context,
                                    "Post is shared successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onClose()
                            }
                            .addOnFailureListener { e ->
                                // Plant data storage failed, show error message
                                Toast.makeText(
                                    context,
                                    "Error sharing: ${e.message}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                    }
                }
        }
    }


        fun bitmapToByteArray(bitmap: Bitmap?): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            return stream.toByteArray()
        }

        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    imageUri = it
                }
            }

        val cameraLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { bitmap ->
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
            BottomSheetItem(
                title = "Choose from library",
                icon = Icons.Outlined.PhotoLibrary,
                onClick = ::chooseFromLibrary,
            ),
            BottomSheetItem(
                title = "Take photo",
                icon = Icons.Outlined.PhotoCamera,
                onClick = ::takePhoto
            ),
            BottomSheetItem(
                title = "Remove current picture",
                icon = Icons.Outlined.Delete,
                onClick = ::removeCurrentPicture
            )
        )
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
        ) {
                BottomSheetScaffold(
                    backgroundColor = Color.White,
                    scaffoldState = bottomSheetScaffoldState,
                    sheetShape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp),
                    sheetContent = {
                        //UI for bottom sheet
                        Column(
                            content = {
                                Spacer(modifier = Modifier.padding(16.dp))
                                Text(
                                    text = "Choose media file",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 21.sp,
                                    color = MaterialTheme.colors.onPrimary
                                )
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(1),
                                ) {
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
                                            Text(
                                                text = bottomSheetItems[it].title,
                                                color = MaterialTheme.colors.onPrimary
                                            )
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
                        IconButton(
                            onClick = { onClose() },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colors.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Spacer(modifier = Modifier.height(5.dp))
                        OutlinedTextField(
                            value = postContent,
                            onValueChange = { postContent = it },
                            label = {
                                Text(
                                    text = "Share your thoughts",
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            },
                            placeholder = { Text(text = "Share your thoughts") },
                            singleLine = false,
                            shape = RoundedCornerShape(1.dp),
                            textStyle =
                            TextStyle(
                                color = MaterialTheme.colors.primary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    } else {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colors.secondary)
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddAPhoto,
                                    contentDescription = "Image",
                                    tint = MaterialTheme.colors.onPrimary
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Add Image",
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                addPost()
                                onClose()
                            },
                            colors = ButtonDefaults.buttonColors(
                                disabledBackgroundColor = MaterialTheme.colors.primary,
                                backgroundColor = MaterialTheme.colors.primary
                            )
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = "Share",
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }


                }
            }
    }


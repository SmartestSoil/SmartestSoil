package com.example.smartestsoil.ui.screens.notes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.smartestsoil.model.CurrentDateTime
import com.example.smartestsoil.model.Note
import com.example.smartestsoil.model.UserSensor
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NotesDialog(
    sensor: UserSensor,
    showNoteDialog: MutableState<Boolean>
) {
    var note by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = FocusRequester()
    val context = LocalContext.current

    fun getNotes() {
        Firebase.firestore.collection("sensors")
            .document(sensor.sensorName)
            .get()
            .addOnSuccessListener { document ->
                val notes = document.toObject<UserSensor>()?.notes ?: emptyList()
                sensor.notes = notes
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error getting notes: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
    fun addNote(title: String, note: String, time: String) {
        val newNote = Note(
            title = title,
            note = note,
            time = time
        )

        Firebase.firestore.collection("sensors")
            .document(sensor.sensorName)
            .update("notes", FieldValue.arrayUnion(newNote))
            .addOnCompleteListener {
                Toast.makeText(context, "Note added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error adding note: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    getNotes()
    Dialog(
        onDismissRequest = { showNoteDialog.value = false }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(state = rememberScrollState())
                .clickable(onClick = {
                    keyboardController?.hide()
                })
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(onClick = { showNoteDialog.value = false }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Notes for ",
                    color = MaterialTheme.colors.primaryVariant,
                    fontSize = 20.sp
                )
                Text(
                    text = sensor.sensorName,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primaryVariant,
                    fontSize = 20.sp,
                )
            }
            for (sensorNote in sensor.notes) {
                Surface(
                    modifier = Modifier.padding(10.dp),
                    color = MaterialTheme.colors.secondary
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = sensorNote.title,
                                color = MaterialTheme.colors.primaryVariant,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = sensorNote.time,
                                color = MaterialTheme.colors.primaryVariant,
                                fontWeight = FontWeight.Medium
                            )
                            IconButton(onClick = { /* TODO */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Edit note",
                                    tint = MaterialTheme.colors.primaryVariant,
                                )
                            }
                        }
                        Row() {
                            Text(
                                text = sensorNote.note,
                                color = MaterialTheme.colors.primaryVariant
                            )
                        }
                    }
                }
            }
            Row() {
                Text(
                    text = "Add a new note",
                    color = MaterialTheme.colors.primaryVariant
                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    time = CurrentDateTime()
                }
            }
            Surface(
                modifier = Modifier.padding(10.dp),
                color = MaterialTheme.colors.background
            ) {
                Column (
                    modifier = Modifier.padding(10.dp)
                ){
                    TextField(
                        modifier = Modifier.padding(5.dp),
                        label = {
                            Text(
                                text = "Enter title",
                                color = MaterialTheme.colors.onPrimary
                            )
                        },
                        value = title,
                        onValueChange = { title = it },
                        singleLine = true,
                        shape = RoundedCornerShape(1.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusRequester.requestFocus() }
                        ),
                        textStyle = TextStyle(color = MaterialTheme.colors.onPrimary)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        modifier = Modifier
                            .padding(5.dp)
                            .focusRequester(focusRequester)
                            .fillMaxWidth(),
                        label = {
                            Text(
                                text = "Enter note",
                                color = MaterialTheme.colors.onPrimary
                            )
                        },
                        value = note,
                        onValueChange = { note = it },
                        singleLine = false,
                        shape = RoundedCornerShape(1.dp),
                        maxLines = 5,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        ),
                        textStyle = TextStyle(color = MaterialTheme.colors.onPrimary)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(
                    onClick = {
                        addNote(
                            title = title,
                            note = note,
                            time = time,
                        )
                        getNotes()
                        title = ""
                        note = ""
                    },
                    enabled = title.isNotBlank() && note.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        disabledBackgroundColor = MaterialTheme.colors.primary,
                        backgroundColor = MaterialTheme.colors.primaryVariant
                    ),
                ) {
                    Text(
                        text = "Add note",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
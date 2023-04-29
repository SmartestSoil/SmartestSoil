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
import androidx.compose.material.icons.outlined.Delete
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.smartestsoil.model.CurrentDateTime
import com.example.smartestsoil.model.Note
import com.example.smartestsoil.model.UserPlant
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NotesDialog(
    plant: UserPlant,
    showNoteDialog: MutableState<Boolean>
) {
    var noteText by remember { mutableStateOf("") }
    var noteTitle by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = FocusRequester()
    val context = LocalContext.current
    time = CurrentDateTime()

    fun getNotes() {
        Firebase.firestore.collection("plants")
            .document(plant.plantName)
            .get()
            .addOnSuccessListener { document ->
                val notes = document.toObject<UserPlant>()?.notes ?: emptyList()
                plant.notes = notes
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

        Firebase.firestore.collection("plants")
            .document(plant.plantName)
            .update("notes", FieldValue.arrayUnion(newNote))
            .addOnCompleteListener {
                Toast.makeText(context, "Note added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error adding note: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun deleteNote(note: Note) {
        val newNotes = plant.notes.filter { it != note }
        Firebase.firestore.collection("plants")
            .document(plant.plantName)
            .update("notes", newNotes)
            .addOnCompleteListener {
                getNotes()
                Toast.makeText(context, "Note deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error deleting note: ${e.message}", Toast.LENGTH_SHORT).show()
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
                    text = plant.plantName,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primaryVariant,
                    fontSize = 20.sp,
                )
            }
            for (plantNote in plant.notes) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    color = MaterialTheme.colors.secondary
                ) {
                    Column (
                        modifier = Modifier.padding(7.dp)
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = plantNote.title,
                                color = MaterialTheme.colors.primaryVariant,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            )
                            Text(
                                text = plantNote.time,
                                color = MaterialTheme.colors.primaryVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = plantNote.note,
                                color = MaterialTheme.colors.primaryVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 5.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(end = 8.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(onClick = { /* TODO */ }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = "Edit note",
                                            tint = MaterialTheme.colors.onPrimary,
                                        )
                                    }
                                    IconButton(onClick = { deleteNote(plantNote) }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = "Delete note",
                                            tint = MaterialTheme.colors.onPrimary,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Add a new note",
                    color = MaterialTheme.colors.primaryVariant,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
                Text(
                    text = CurrentDateTime(),
                    color = MaterialTheme.colors.primaryVariant,
                    fontSize = 18.sp
                )
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
                        value = noteTitle,
                        onValueChange = { noteTitle = it },
                        singleLine = true,
                        shape = RoundedCornerShape(1.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words
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
                        value = noteText,
                        onValueChange = { noteText = it },
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
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        addNote(
                            title = noteTitle,
                            note = noteText,
                            time = time,
                        )
                        getNotes()
                        noteTitle = ""
                        noteText = ""
                    },
                    enabled = noteTitle.isNotBlank() && noteText.isNotBlank(),
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
package com.example.smartestsoil.ui.screens.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartestsoil.model.UserPlant

@Composable
fun NotesButton(plant: UserPlant) {
    var showNoteDialog = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .padding(10.dp)
            .width(180.dp)
            .clickable(onClick = { showNoteDialog.value = true })
            .height(60.dp),
        elevation = 8.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        ) {

            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "add plant record",
                color = MaterialTheme.colors.onPrimary,
                fontSize = 16.sp
            )
        }
    }
    if (showNoteDialog.value) {
        NotesDialog(plant, showNoteDialog)
    }
}
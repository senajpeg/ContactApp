package com.senaaksoy.mycontacts.roomDbProcess

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mkrdeveloper.roomdatabasecrashcourse.R

@Composable
fun AddContactDialog(
    onDismiss: () -> Unit,
    onAddContact: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(id = R.string.add_new_contact)) },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = stringResource(id = R.string.name_and_surname))}
                )
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text(stringResource(id = R.string.phone_number)) }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onAddContact(name, phoneNumber)
                onDismiss() },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.DarkGray
                    ,containerColor = Color(0xFF558056))
            ) {
                Text(stringResource(id = R.string.add))
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.DarkGray
                    ,containerColor = Color(0xFF558056))) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}

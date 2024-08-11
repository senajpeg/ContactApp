package com.senaaksoy.mycontacts.roomDbProcess

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mkrdeveloper.roomdatabasecrashcourse.R

@Composable
fun DeleteContactDialog(
    contactName: String,
    onDismiss: () -> Unit,
    onDeleteContact: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.delete_contact))
        },
        text = {
            Text(text = stringResource(id = R.string.are_you_sure)+"'$contactName'?")
        },
        confirmButton = {
            Button(
                onClick = {
                    onDeleteContact()
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.DarkGray,
                    containerColor = Color(0xFF558056)
                )
            ) {
                Text(stringResource(id = R.string.delete))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.DarkGray,
                    containerColor = Color(0xFF558056)
                )
            ) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}

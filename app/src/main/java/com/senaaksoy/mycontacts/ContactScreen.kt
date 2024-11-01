package com.senaaksoy.mycontacts

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mkrdeveloper.roomdatabasecrashcourse.R
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.senaaksoy.mycontacts.roomDbProcess.AddContactDialog
import com.senaaksoy.mycontacts.roomDbProcess.DeleteContactDialog
import com.senaaksoy.mycontacts.roomDbProcess.EditContactDialog
import com.senaaksoy.mycontacts.roomdb.Contact
import com.senaaksoy.mycontacts.viewModel.ContactViewModel

@Composable
fun ContactScreen(viewModel: ContactViewModel) {

    val contacts by viewModel.getContacts().observeAsState(emptyList())

    var showDialog by remember { mutableStateOf(false) }

    var contactToDelete by remember { mutableStateOf<Contact?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var showEditDialog by remember { mutableStateOf(false) }
    var contactToEdit by remember { mutableStateOf<Contact?>(null) }

    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    var showRecentCallsDialog by remember { mutableStateOf(false) }

    val recentCalls by remember { mutableStateOf(viewModel.recentCalls) }

    // filtered list
    val filteredContacts = contacts.filter { contact ->
        contact.name.contains(searchQuery, ignoreCase = true)
    }
    /* if searchQuery is  null, contacts.filter { contact -> contact.name.contains(searchQuery, ignoreCase = true) }
   returns all contacts list. because a null searchQuery matches with everything*/


    Scaffold(
        topBar = {
            MyTopAppBar(
            onAddContactClick = { showDialog = true },
            onSearchClick = { showSearch = !showSearch } ,
                onRecentCallsClick = {showRecentCallsDialog=true}// when clicking on search icon,show/hide search box
            )    }

    ) {
        paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)
    ) {
    // Search Box
    if (showSearch) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(id = R.string.search)) }
        )
    }
        LazyColumn {
            items(filteredContacts) { contact ->
                ContactItem(
                    contact = contact,
                    onDeleteContactClick = {
                        contactToDelete = contact
                        showDeleteDialog = true
                    },
                    onEditContactClick = {
                        contactToEdit=contact
                        showEditDialog=true
                    },
                    onPhoneCallClick = {viewModel.addRecentCall(contact.name)}
                )
                Divider(color = Color.LightGray, thickness = 2.dp)
            }
        }
    }
        if (showDialog) {
            AddContactDialog(
                onDismiss = { showDialog = false },
                onAddContact = { name, phoneNumber ->
                    val newContact = Contact(name = name, phoneNumber = phoneNumber)
                    viewModel.insertContact(newContact)
                    showDialog = false
                }
            )
        }
        if (showDeleteDialog && contactToDelete != null) {
            DeleteContactDialog(
                contactName = contactToDelete!!.name,
                onDismiss = { showDeleteDialog = false },
                onDeleteContact = {
                    viewModel.deleteContact(contactToDelete!!)
                    showDeleteDialog = false
                    contactToDelete = null
                }
            )
        }
        if(showEditDialog && contactToEdit !=null){
            EditContactDialog(contact = contactToEdit!!,
                onDismiss = { showEditDialog=false },
                onEditContact = { name ,phoneNumber ->
                    val updatedContact =contactToEdit!!.copy(name = name, phoneNumber = phoneNumber)
                    viewModel.updateContact(updatedContact)
                    showEditDialog=false
                    contactToEdit=null
                }
                )
        }
        if (showRecentCallsDialog) {
            AlertDialog(
                onDismissRequest = { showRecentCallsDialog = false },
                title = { Text(text = stringResource(id = R.string.recent_calls)) },
                text = {
                    Column {
                       recentCalls.forEach { call ->
                            Text(text = call, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showRecentCallsDialog = false }) {
                        Text(text= stringResource(id = R.string.ok))
                    }
                }
            )
        }
    }
}


@Composable
fun ContactItem(
    contact: Contact,
    modifier: Modifier=Modifier,
    onDeleteContactClick: (Contact) -> Unit,
    onEditContactClick: (Contact) -> Unit,
    onPhoneCallClick:()->Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if (expanded)
            Color(0xFFDDE0EE) else Color(0xFFB0BAF0)
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RectangleShape

    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                .background(color = color)
        ) {
              Row(modifier = modifier
                  .fillMaxWidth()
                  .padding(8.dp)) {
                  Text(text = contact.name, fontWeight = FontWeight.Bold)
                  Spacer(Modifier.weight(1f))
                  IconButton(onClick = { onEditContactClick(contact) }) {
                      Icon(imageVector = Icons.Filled.Edit,contentDescription = null)
                      
                  }
                  IconButton(onClick = { onDeleteContactClick(contact) }) {
                      Icon(imageVector = Icons.Filled.Delete, contentDescription =null )
                  }

              }
            if (expanded) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text =  (stringResource(id = R.string.phone_number))+contact.phoneNumber, modifier = modifier.padding(4.dp))
                    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Imageicon(onClick = {onPhoneCallClick()}, imageicon = R.drawable.phone_call)
                        Imageicon(onClick = {}, imageicon = R.drawable.circle)
                        Imageicon(onClick = {}, imageicon = R.drawable.video_call)
                    }

                }
            }

        }
    }
}
@Composable
fun Imageicon(
    @DrawableRes imageicon : Int,
    onClick: ()->Unit,
    modifier: Modifier=Modifier
){
    Image( modifier = modifier
        .padding(12.dp)
        .size(32.dp)
        .clip(MaterialTheme.shapes.small)
        .clickable { onClick() },
        painter = painterResource(imageicon),
        contentDescription =null)

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(onAddContactClick: () -> Unit,
                onSearchClick: () -> Unit,
                onRecentCallsClick: () -> Unit) {
    TopAppBar(
        title =
        {
            Text(
                text = stringResource(id = R.string.contacs),
                style = MaterialTheme.typography.titleLarge,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold
            )
        },
        actions ={
            IconButton(onClick = {onAddContactClick() }) {
                Icon(imageVector = Icons.Filled.Add,
                    contentDescription = null
                   )
            }
            IconButton(onClick = { onSearchClick() }) {
                Icon(imageVector = Icons.Filled.Search,
                    contentDescription = null)

            }
            IconButton(onClick = { onRecentCallsClick() }) {
                Icon(imageVector = Icons.Filled.Phone, contentDescription = null)
                
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF171E44),
            titleContentColor = Color(0xFFACB3DA), actionIconContentColor = Color(0xFFACB3DA))
    )
}
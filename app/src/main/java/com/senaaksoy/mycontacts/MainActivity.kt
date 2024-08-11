package com.senaaksoy.mycontacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.senaaksoy.mycontacts.roomdb.ContactDatabase
import com.senaaksoy.mycontacts.ui.theme.MyContactsTheme
import com.senaaksoy.mycontacts.viewModel.ContactViewModel
import com.senaaksoy.mycontacts.viewModel.Repository

class MainActivity : ComponentActivity() {
    private val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
        name="contact.db").build()
    }
    private val viewModel by viewModels<ContactViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ContactViewModel(Repository(db)) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyContactsTheme {
                ContactScreen(viewModel=viewModel)
                }
            }
        }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyContactsTheme {

    }
}
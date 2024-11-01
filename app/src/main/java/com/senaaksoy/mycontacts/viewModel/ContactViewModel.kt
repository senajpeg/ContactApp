package com.senaaksoy.mycontacts.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.senaaksoy.mycontacts.roomdb.Contact
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: Repository) : ViewModel()  {

    val initialContacts = listOf(
        Contact(name = "Alice Johnson", phoneNumber = "530 123 45 67"),
        Contact(name = "Andrew Smith", phoneNumber = "531 234 56 78"),
        Contact(name = "Amelia Brown", phoneNumber = "532 345 67 89"),
        Contact(name = "Brian Clark", phoneNumber = "533 456 78 90"),
        Contact(name = "Bella Adams", phoneNumber = "534 567 89 01"),
        Contact(name = "Benjamin Miller", phoneNumber = "535 678 90 12"),
        Contact(name = "Charlie Wilson", phoneNumber = "536 789 01 23"),
        Contact(name = "Chloe Evans", phoneNumber = "537 890 12 34"),
        Contact(name = "Caleb Lewis", phoneNumber = "538 901 23 45"),
        Contact(name = "Daniel Scott", phoneNumber = "539 012 34 56"),
        Contact(name = "Daisy Walker", phoneNumber = "530 987 65 43"),
        Contact(name = "David Moore", phoneNumber = "531 876 54 32"),
        Contact(name = "Emma Taylor", phoneNumber = "532 765 43 21"),
        Contact(name = "Ethan King", phoneNumber = "533 654 32 10"),
        Contact(name = "Eva Thompson", phoneNumber = "534 543 21 09"),
        Contact(name = "Fiona Harris", phoneNumber = "535 432 10 98"),
        Contact(name = "Frank White", phoneNumber = " 536 321 09 87"),
        Contact(name = "Florence Green", phoneNumber = "537 210 98 76"),

        )


    private val _recentCalls = mutableStateListOf<String>()
    val recentCalls: List<String> get() = _recentCalls

    fun addRecentCall(contactName: String) {
        _recentCalls.add(contactName)
        if (_recentCalls.size > 20) {
            _recentCalls.removeAt(0)
        }
    }


    init {
        viewModelScope.launch {
            val existingContacts = repository.getAllContacts().first()
            if (existingContacts.isEmpty()) {
                repository.insertAll(initialContacts)
            }
        }
    }

    fun getContacts()=repository.getAllContacts().asLiveData(viewModelScope.coroutineContext)


    fun insertContact(contact: Contact) {
        viewModelScope.launch {
            repository.insert(contact)
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.delete(contact)
        }
    }
    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            repository.updateContact(contact)
        }
    }
}






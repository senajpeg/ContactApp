package com.senaaksoy.mycontacts.viewModel

import com.senaaksoy.mycontacts.roomdb.Contact
import com.senaaksoy.mycontacts.roomdb.ContactDatabase
import kotlinx.coroutines.flow.Flow



class Repository(private val db : ContactDatabase) {


    suspend fun insert(contact: Contact){
    db.dao.insert(contact)
    }
    suspend fun delete(contact: Contact){
        db.dao.delete(contact)
    }
    fun getAllContacts()=db.dao.getAllContacts()
    suspend fun updateContact(contact: Contact) {
        db.dao.updateContact(contact)
    }

    suspend fun insertAll(contacts: List<Contact>) {
        db.dao.insertAll(contacts)
    }
}
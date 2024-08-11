package com.senaaksoy.mycontacts.viewModel

import com.senaaksoy.mycontacts.roomdb.Contact
import com.senaaksoy.mycontacts.roomdb.ContactDatabase

class Repository(private val db : ContactDatabase) {


    suspend fun insert(contact: Contact){
    db.dao.insert(contact)
    }
    suspend fun delete(contact: Contact){
        db.dao.delete(contact)
    }
    fun getAllContacts()=db.dao.getAllContacts()

    suspend fun insertAll(contacts: List<Contact>) {
        db.dao.insertAll(contacts)
    }
}
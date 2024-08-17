package com.senaaksoy.mycontacts

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.senaaksoy.mycontacts.roomdb.Contact
import com.senaaksoy.mycontacts.roomdb.ContactDao
import com.senaaksoy.mycontacts.roomdb.ContactDatabase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//  bu testin Android ortamında çalışacağını söylüyor.

@RunWith(AndroidJUnit4::class)
class ContactDatabaseTest {

    private lateinit var contactDao: ContactDao
    private lateinit var contactDatabase: ContactDatabase

    @Before
    fun setUp() {
        contactDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ContactDatabase::class.java
        ).allowMainThreadQueries().build()

        contactDao = contactDatabase.dao
    }

    @After
    fun tearDown() {
        contactDatabase.close()
    }

    @Test
    fun insertAndGetContact() = runTest {
        val contact = Contact(name = "John Doe", phoneNumber = "123456789")
        contactDao.insert(contact)

        val allContacts = contactDao.getAllContacts().first()
        assertEquals(1, allContacts.size)
        assertEquals("John Doe", allContacts[0].name)
    }

    @Test
    fun updateContact() = runTest {
        val contact = Contact(name = "John Doe", phoneNumber = "123456789")
        contactDao.insert(contact)

        val insertedContact = contactDao.getAllContacts().first().first()
        val updatedContact = insertedContact.copy(name = "James Smith", phoneNumber = "987654321")
        contactDao.updateContact(updatedContact)

        val allContacts = contactDao.getAllContacts().first()
        assertEquals(1, allContacts.size)
        assertEquals("James Smith", allContacts[0].name)
        assertEquals("987654321", allContacts[0].phoneNumber)
    }

    @Test
    fun deleteContact() = runTest {
        val contact = Contact(name = "Jane Doe", phoneNumber = "987654321")
        contactDao.insert(contact)

        val insertedContact = contactDao.getAllContacts().first().first()
        contactDao.delete(insertedContact)

        val allContacts = contactDao.getAllContacts().first()
        assertTrue(allContacts.isEmpty())
    }
}
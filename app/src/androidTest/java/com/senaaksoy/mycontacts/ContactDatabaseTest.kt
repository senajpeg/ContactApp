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

    //@Before un içindeki fonksiyon,test sınıfındaki her test fonksiyonundan önce çalışır
    @Before
    //setUp() da test ortamını ayarlar
    fun setUp() {
        /*inMemoryDatabaseBuilder,bellekte çalışan geçiçi bi veritabanı oluşturuyor.
        * Böylece testler her çalıştığında temiz bi veritabanıyla başlamış oluyor. */

        contactDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ContactDatabase::class.java
        ).allowMainThreadQueries().build()

/* allowMainThreadQueries,normalde veritabanı işlemleri arka planda yapılır ama bu ile ana iş
* parçacığında yapılır.*/

        contactDao = contactDatabase.dao
        //burada ContactDao nesnesini veritabanından alıyor.bu contactDao,veritabanı işlemleri için kullanılacak.
    }

    /* @After,her test fonksiyonundan sonra çalışıcak demek*/
    @After
    fun tearDown() {
        // Veritabanı testi sonrası kaynakların serbest bırakılması
        contactDatabase.close()
    }

    // @Test,bu fonksiyonun bi test fonksiyonu olduğunu belirtiyor
    @Test
    fun insertAndGetContact() = runTest {
        // Test verisi oluşturma
        val contact = Contact(name = "John Doe", phoneNumber = "123456789")
        contactDao.insert(contact)

        // Veritabanından veriyi çekme ve doğrulama
        val allContacts = contactDao.getAllContacts().first()
        //1 beklenen kişi sayısı değeri,diğer parametre de dönen kişi sayısı.
        assertEquals(1, allContacts.size)
        assertEquals("John Doe", allContacts[0].name)
    }

    @Test
    fun updateContact() = runTest {
        // İlk veriyi ekliyorz
        val contact = Contact(name = "John Doe", phoneNumber = "123456789")
        contactDao.insert(contact)

        // Veriyi güncelleyin
        val updatedContact = contact.copy(name = "James Smith", phoneNumber = "987654321")
        contactDao.updateContact(updatedContact)

        // Güncellenmiş veriyi alıp doğrulama yapın
        val allContacts = contactDao.getAllContacts().first()
        assertEquals(1, allContacts.size)
        assertEquals("James Smith", allContacts[0].name)
        assertEquals("987654321", allContacts[0].phoneNumber)
    }

    @Test
    fun deleteContact() = runTest {
        val contact = Contact(name = "Janes Doe", phoneNumber = "987654321")
            contactDao.insert(contact)

            contactDao.delete(contact)

       val allContacts = contactDao.getAllContacts().first()
        assertTrue(allContacts.isEmpty())
    }

      /*   if (allContacts.isEmpty()) {
            println("It is empty")
        } else {
            println("Contacts found: $allContacts")
        }*/



    }


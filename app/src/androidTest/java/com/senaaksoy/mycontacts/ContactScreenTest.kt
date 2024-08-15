package com.senaaksoy.mycontacts

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.senaaksoy.mycontacts.roomdb.Contact
import com.senaaksoy.mycontacts.roomdb.ContactDatabase
import com.senaaksoy.mycontacts.viewModel.ContactViewModel
import com.senaaksoy.mycontacts.viewModel.Repository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ContactScreenTest {

    @get:Rule
    //bu kural,testin compose arayüzünde yapılmasını sağlar.
    val composeTestRule = createComposeRule()

    private lateinit var contactDatabase: ContactDatabase
    private lateinit var contactViewModel: ContactViewModel //veriyi tutacak,ui'a verecek.

    @Before
    fun setUp() {
        contactDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ContactDatabase::class.java
        ).allowMainThreadQueries().build()

       //geçici veritabanına iki kişi ekleniyor
        runTest {
            contactDatabase.dao.insert(Contact(name = "Alice Johnson", phoneNumber = "530 123 45 67"))
            contactDatabase.dao.insert(Contact(name = "Andrew Smith", phoneNumber = "531 234 56 78"))
        }

        /*bu adımda veritabanından gelen verileri arayüze verecek viewmodel nesnesini oluşturuyoruz
        * ve veritabanıyla diğer işlemleri birbirine bağlayan repoyu kullanıyoruz. reodaki parantezin içine de hangi
        * veritabanını kullanacağımızı seçiyoruz.burada geçiçi veritabanını gönderdik */
        contactViewModel = ContactViewModel(Repository(contactDatabase))
    }

    @After
    fun tearDown() {
        contactDatabase.close()
    }

    //bu fonk,asıl testin yapıldığı yer
    @Test
    fun testContactListIsDisplayed() {
        //burada ui'ı oluşturuyoruz
        composeTestRule.setContent {
            ContactScreen(viewModel = contactViewModel)
        }

        // listeye eklediğimiz kişilerin arayüzde görünüp görünmediğini doğruluyor.
        composeTestRule.onNodeWithText("Alice Johnson").assertExists()
        composeTestRule.onNodeWithText("Andrew Smith").assertExists()
    }
}

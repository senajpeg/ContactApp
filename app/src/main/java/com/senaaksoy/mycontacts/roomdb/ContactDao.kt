package com.senaaksoy.mycontacts.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
//gpt ile düzelen kısım
import kotlinx.coroutines.flow.Flow


@Dao
interface ContactDao {
    @Insert
    suspend fun insert(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)
    @Update
    suspend fun updateContact(contact: Contact)

 @Query("SELECT * FROM contacts ORDER BY name ASC ") //burdaki isim tablo adımızla aynı olmalı..
 //kişilerin isimlerini alfabetik sırıaya göre getircek asc li kısım
 fun getAllContacts() : Flow<List<Contact>>

 @Insert
 suspend fun insertAll(contacts: List<Contact>)
 }

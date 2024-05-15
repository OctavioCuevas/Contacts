package com.mx.crystalcloud.contactsgs.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts")
    fun getAllUsers(): List<Contact>

    @Insert
    fun insertContact(contact: Contact)
}
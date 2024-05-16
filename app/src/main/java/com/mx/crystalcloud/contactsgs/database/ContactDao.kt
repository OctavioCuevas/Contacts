package com.mx.crystalcloud.contactsgs.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllUsers(): List<Contact>

    @Query("SELECT * FROM contacts ORDER BY lastName ASC")
    fun getAllUsersByLastName(): List<Contact>

    @Query("SELECT * FROM contacts ORDER BY age DESC")
    fun getAllUsersByAge(): List<Contact>

    @Insert
    fun insertContact(contact: Contact)

    @Update
    fun updateContact(contact: Contact)
}
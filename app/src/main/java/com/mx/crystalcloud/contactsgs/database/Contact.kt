package com.mx.crystalcloud.contactsgs.database
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val lastName: String,
    val motherLastName: String?,
    val age: Int?,
    val phone: String,
    val gender: String?,
    val photo: String?,
) : Parcelable

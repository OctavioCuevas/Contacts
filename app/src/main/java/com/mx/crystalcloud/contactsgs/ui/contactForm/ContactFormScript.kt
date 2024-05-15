package com.mx.crystalcloud.contactsgs.ui.contactForm

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.sql.SQLException
/*
class ContactFormScript(val dbFile: File) : Database() {

    suspend fun insertContact(contact: ContactModel) {
        withContext(Dispatchers.IO) {
            val motherLastName = contact.motherLastName ?: "null"
            val age = contact.age ?: "null"
            val photo = contact.photo ?: "null"
            val sql = """
                INSERT INTO contacts (name, last_name, mother_last_name, age, phone, gender, photo)
                VALUES ('${contact.name}', '${contact.lastName}', '$motherLastName', '$age', '${contact.phone}', '${contact.gender}', '$photo');
            """.trimIndent()
            val conn = connect(dbFile)
            try {
                val stmt = conn.createStatement()
                stmt.executeUpdate(sql)
            } catch (e: SQLException) {
                Log.e("cct error insert", "Ocurrió un error al insertar:", e)
            } finally {
                conn.close()
            }
        }
    }

    fun getAllContacts(): List<ContactModel> {
        Log.e("cct", "here")
        val sql = "SELECT * FROM contacts;"
        val conn = connect(dbFile)
        val contacts = mutableListOf<ContactModel>()
        try {
            val stmt = conn.createStatement()
            val resultSet = stmt.executeQuery(sql)

            while (resultSet.next()) {
                Log.e("cct contacto", resultSet.getString("nombre"))
                val id = resultSet.getInt("id")
                val nombre = resultSet.getString("nombre")
                val telefono = resultSet.getString("telefono")
                val email = resultSet.getString("email")
                //contacts.add(ContactModel(id, nombre, telefono, email))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            conn.close()
        }
        return contacts
    }
}
*/
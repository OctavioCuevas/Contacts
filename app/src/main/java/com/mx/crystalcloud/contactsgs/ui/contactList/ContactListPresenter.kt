package com.mx.crystalcloud.contactsgs.ui.contactList

import android.content.Context
import android.util.Log
import androidx.navigation.NavController
import com.mx.crystalcloud.contactsgs.database.ContactsDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ContactListPresenter(private val view: ContactListView) {
    fun setContactsAdapter(context: Context, navController: NavController) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val contacts = ContactsDatabase.getInstance(context).contactsDao.getAllUsers()
                if (contacts.isNotEmpty()) {
                    view.setContacts(ContactListAdapter(contacts, navController))
                }
            } catch (e: Exception) {
                Log.e("cct", "ocurrió error al insertar: " + e.message)
            }
        }
    }
}
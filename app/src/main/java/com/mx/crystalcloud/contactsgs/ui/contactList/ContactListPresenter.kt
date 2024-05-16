package com.mx.crystalcloud.contactsgs.ui.contactList

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import androidx.navigation.NavController
import com.mx.crystalcloud.contactsgs.database.ContactsDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactListPresenter(private val view: ContactListView) {

    private var orderOptions = listOf("Nombre", "Apellido", "Edad")

    fun setContactsAdapter(context: Context, navController: NavController, orderBy: OrderBy) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val contacts = when(orderBy) {
                    OrderBy.NAME -> ContactsDatabase.getInstance(context).contactsDao.getAllUsers()
                    OrderBy.LAST_NAME -> ContactsDatabase.getInstance(context).contactsDao.getAllUsersByLastName()
                    OrderBy.AGE -> ContactsDatabase.getInstance(context).contactsDao.getAllUsersByAge()
                }

                if (contacts.isNotEmpty()) {

                    view.setContacts(ContactListAdapter(contacts, navController))
                }
            } catch (e: Exception) {
                Log.e("cct", "ocurrió error al obtener contactos: " + e.message)
            }
        }
    }

    fun setOrderByAdapter(context: Context) {
        val spinnerAdapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, orderOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.setOrderByOptions(spinnerAdapter)
    }

    companion object {
        enum class OrderBy {
            NAME, LAST_NAME, AGE
        }
    }
}
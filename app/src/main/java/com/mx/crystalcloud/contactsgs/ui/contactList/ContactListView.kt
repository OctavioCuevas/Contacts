package com.mx.crystalcloud.contactsgs.ui.contactList

import android.widget.SpinnerAdapter

interface ContactListView {
    fun setContacts(contactsAdapter: ContactListAdapter)

    fun setOrderByOptions(spinnerAdapter: SpinnerAdapter)
}
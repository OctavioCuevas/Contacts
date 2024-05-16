package com.mx.crystalcloud.contactsgs.ui.contactForm

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Adapter
import android.widget.SpinnerAdapter
import com.mx.crystalcloud.contactsgs.database.Contact

interface ContactFormView {
    fun setGenders(spinnerAdapter: SpinnerAdapter)

    fun backToContactList()

    fun setTakenPhoto(imageBitmap: Bitmap)

    fun setSelectedPhoto(imageUri: Uri)

    fun setError(type: MissingField)

    fun setContactData(contact: Contact)

    fun setScreenStrings(isNewContact: Boolean)

}

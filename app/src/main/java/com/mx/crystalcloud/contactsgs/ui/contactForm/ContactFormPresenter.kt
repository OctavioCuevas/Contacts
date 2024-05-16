package com.mx.crystalcloud.contactsgs.ui.contactForm

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.util.Log
import android.widget.ArrayAdapter
import com.mx.crystalcloud.contactsgs.database.Contact
import com.mx.crystalcloud.contactsgs.database.ContactsDatabase
import com.mx.crystalcloud.contactsgs.ui.contactList.ContactListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

class ContactFormPresenter(private val view: ContactFormView) {
    private var selectedImageUri: Uri? = null
    private var selectedGender: String? = null
    private var genderOptions = listOf("Hombre", "Mujer")
    private var isNewContact = true
    private var contactId = 0

    fun formButtonClicked(
        context: Context,
        name: Editable?,
        lastName: Editable?,
        motherLastName: Editable?,
        age: Editable?,
        phone: Editable?,
    ) {
        var formFilled = true
        if (name.toString().isEmpty()) {
            view.setError(MissingField.NAME)
            formFilled = false
        }
        if (lastName.toString().isEmpty()) {
            view.setError(MissingField.LAST_NAME)
            formFilled = false
        }
        if (phone.toString().isEmpty()) {
            view.setError(MissingField.PHONE)
            formFilled = false
        }
        if (formFilled) {
            val contactAge = age?.toString()?.let {
                if (it.isNotEmpty()) it.toInt() else null
            }
            val contact = Contact(
                contactId,
                name = name.toString(),
                lastName = lastName.toString(),
                motherLastName = motherLastName.toString(),
                age = contactAge,
                phone = phone.toString(),
                gender = selectedGender,
                photo = selectedImageUri?.path,
            )
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if (isNewContact) {
                        ContactsDatabase.getInstance(context).contactsDao.insertContact(contact)
                    } else {
                        ContactsDatabase.getInstance(context).contactsDao.updateContact(contact)
                    }
                    view.backToContactList()
                } catch (e: Exception) {
                    Log.e("cct", "ocurrió error al insertar: " + e.message)
                }
            }
        }
    }

    fun setGenderAdapter(context: Context) {
        val spinnerAdapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, genderOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.setGenders(spinnerAdapter)
    }

    fun setSelectedGender(position: Int) {
        selectedGender = genderOptions[position]
    }

    fun saveTakenPhoto(bundle: Bundle) {
        val imageBitmap = bundle.getParcelable("data") as Bitmap?
        imageBitmap?.let { image ->
            selectedImageUri = saveImageToInternalStorage(imageBitmap)
            view.setTakenPhoto(image)
        }
    }

    fun setSelectedPhoto(intent: Intent) {
        intent.data?.let {
            selectedImageUri = it
            view.setSelectedPhoto(it)
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        val picturesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val appDir = File(picturesDir, FOLDER)
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        val imageFile = File(appDir, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            return null
        }
        return Uri.parse(imageFile.absolutePath)
    }

    fun setContact(args: Bundle?) {
        args?.let {
            val receivedContact = it.getParcelable<Contact>(ContactListAdapter.B_CONTACT)
            receivedContact?.let { contact ->
                view.setContactData(contact)
                contactId = contact.id
                isNewContact = false
            }
        }
        view.setScreenStrings(isNewContact)
    }

    companion object {
        private const val FOLDER = "Contacts"
    }
}

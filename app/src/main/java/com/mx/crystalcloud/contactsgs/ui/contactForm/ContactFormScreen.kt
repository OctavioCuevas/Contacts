package com.mx.crystalcloud.contactsgs.ui.contactForm

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.SpinnerAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mx.crystalcloud.contactsgs.R
import com.mx.crystalcloud.contactsgs.databinding.ContactFormScreenBinding
import kotlinx.coroutines.launch

class ContactFormScreen : Fragment(), ContactView {
    private var _binding: ContactFormScreenBinding? = null

    private val binding get() = _binding!!
    private lateinit var presenter: ContactPresenter

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.extras?.let {
                    presenter.saveTakenPhoto(it)
                }
            }
        }

    private val selectGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                result.data?.let {
                    presenter.setSelectedPhoto(it)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ContactFormScreenBinding.inflate(inflater, container, false)
        presenter = ContactPresenter(this)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setGenders(spinnerAdapter: SpinnerAdapter) = with(binding.genderSpinner) {
        adapter = spinnerAdapter
    }

    override fun backToContactList() {
        findNavController().navigate(R.id.action_contact_form_to_contact_list)
        findNavController().popBackStack()
    }

    override fun setSelectedPhoto(imageUri: Uri) = with(binding.contactPhoto) {
        setImageURI(imageUri)
    }

    override fun setError(type: MissingField) = with(binding) {
        when (type) {
            MissingField.NAME -> nameLayout.error = getString(R.string.contact_missing_data_error)

            MissingField.LAST_NAME -> lastNameLayout.error =
                getString(R.string.contact_missing_data_error)

            MissingField.PHONE -> phoneLayout.error = getString(R.string.contact_missing_data_error)
        }
    }

    override fun setTakenPhoto(imageBitmap: Bitmap) = with(binding.contactPhoto) {
        setImageBitmap(imageBitmap)
    }

    private fun initComponents() = with(binding) {
        presenter.setGenderAdapter(requireContext())

        saveContactButton.setOnClickListener {
            lifecycleScope.launch {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    Log.e("Cct", "nosta")
                }
                presenter.formButtonClicked(
                    requireContext(),
                    nameEditText.text,
                    lastNameEditText.text,
                    motherLastNameEditText.text,
                    ageEditText.text,
                    phoneEditText.text,
                )
            }
        }

        contactPhoto.setOnClickListener {
            showPopupMenu()
        }

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                presenter.setSelectedGender(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Unused function
            }
        }

        nameEditText.doOnTextChanged { _, _, _, _ ->
            nameLayout.isErrorEnabled = false
        }

        lastNameEditText.doOnTextChanged { _, _, _, _ ->
            lastNameLayout.isErrorEnabled = false
        }

        phoneEditText.doOnTextChanged { _, _, _, _ ->
            phoneLayout.isErrorEnabled = false
        }
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.contactPhoto)
        popupMenu.menuInflater.inflate(R.menu.photo_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_take_photo -> {
                    takePhoto()
                    true
                }

                R.id.menu_select_gallery -> {
                    selectFromGallery()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                PERMISSIONS_REQUEST_CODE,
            )
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureLauncher.launch(takePictureIntent)
        }
    }

    private fun selectFromGallery() {
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectGalleryLauncher.launch(pickPhotoIntent)
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 1001
        private const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1002
    }
}

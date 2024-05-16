package com.mx.crystalcloud.contactsgs.ui.contactList

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.mx.crystalcloud.contactsgs.R
import com.mx.crystalcloud.contactsgs.databinding.ContactListScreenBinding

class ContactListScreen : Fragment(), ContactListView {
    private var _binding: ContactListScreenBinding? = null

    private lateinit var presenter: ContactListPresenter

    private val binding get() = _binding!!
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.e("Cct", "Permiso de escritura aceptado")
            // El usuario concedió el permiso, puedes proceder con tu lógica
        } else {
            // El usuario denegó el permiso, puedes mostrar un mensaje o realizar alguna acción adicional
            Log.e("Cct", "Permiso de escritura denegado")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ContactListScreenBinding.inflate(inflater, container, false)
        presenter = ContactListPresenter(this)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()

        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initComponents() = with(binding) {
        presenter.setContactsAdapter(
            requireContext(), findNavController(), ContactListPresenter.Companion.OrderBy.NAME
        )
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_contact_list_to_contact_form)
        }
        val toolbar: MaterialToolbar? = activity?.findViewById(R.id.toolbar)
        toolbar?.let {
            it.title = getString(R.string.contact_list_title)
        }
        presenter.setOrderByAdapter(requireContext())

        orderBySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val orderBy = when (position) {
                    1 -> ContactListPresenter.Companion.OrderBy.LAST_NAME
                    2 -> ContactListPresenter.Companion.OrderBy.AGE
                    else -> ContactListPresenter.Companion.OrderBy.NAME
                }
                presenter.setContactsAdapter(requireContext(), findNavController(), orderBy)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Unused function
            }
        }
    }

    override fun setOrderByOptions(spinnerAdapter: SpinnerAdapter) = with(binding.orderBySpinner) {
        adapter = spinnerAdapter
    }

    override fun setContacts(contactsAdapter: ContactListAdapter) = with(binding.contactList) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = contactsAdapter
    }

}

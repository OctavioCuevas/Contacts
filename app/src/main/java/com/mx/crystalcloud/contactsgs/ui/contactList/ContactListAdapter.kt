package com.mx.crystalcloud.contactsgs.ui.contactList

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.mx.crystalcloud.contactsgs.R
import com.mx.crystalcloud.contactsgs.database.Contact
import com.mx.crystalcloud.contactsgs.databinding.ContactItemBinding


class ContactListAdapter(private val dataList: List<Contact>, private val navController: NavController) : RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable(B_CONTACT, dataList[position])
            }
            navController.navigate(R.id.action_contact_list_to_contact_form, bundle)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(private val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = itemView.context

        fun bind(item: Contact) = with(binding) {
            contactName.text = context.getString(R.string.contact_list_item_full_name, item.name, item.lastName, item.motherLastName)
            if (item.age != null) {
                contactAgeAndGender.text = context.getString(R.string.contact_list_item_age_and_gender, item.gender, item.age)
            } else {
                contactAgeAndGender.text = item.gender
            }
            contactPhone.text = item.phone

            item.photo?.let { imageStr ->
                image.setImageURI(Uri.parse(imageStr))
                Log.e("cct", imageStr)
            }

            deleteImgButton.setOnClickListener {
                Log.e("cct", "eliminar")
            }
        }
    }
    companion object {
        const val B_CONTACT = "contact"
    }
}
package com.burtaev.application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf

private const val ARG_PARAM_CONTACT_ID = "contactId"

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {
    companion object {
        fun newInstance(contactId: Long) = ContactDetailsFragment().apply {
            arguments = bundleOf(ARG_PARAM_CONTACT_ID to contactId)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ivPhoto = view.findViewById<ImageView>(R.id.ivContactPhoto)
        val tvName = view.findViewById<TextView>(R.id.tvContactName)
        val tvPhone = view.findViewById<TextView>(R.id.tvContactPhone)
        val tvPhone2 = view.findViewById<TextView>(R.id.tvContactPhone2)
        val tvEmail = view.findViewById<TextView>(R.id.tvContactEmail)
        val tvEmail2 = view.findViewById<TextView>(R.id.tvContactEmail2)
        val tvDescription = view.findViewById<TextView>(R.id.tvContactDescription)
        val contactId: Long = arguments?.getLong(ARG_PARAM_CONTACT_ID, 0) ?: 0
        val contactsDataSource: ContactsDataSource = ContactModel()
        val contact: Contact? = contactsDataSource.getContactById(contactId)
        if (contact != null) {
            ivPhoto.setImageResource(contact.img)
            tvName.text = contact.name
            tvPhone.text = contact.phoneNum
            tvPhone2.text = contact.phoneNum2
            tvEmail.text = contact.email
            tvEmail2.text = contact.email2
            tvDescription.text = contact.description
        }
        requireActivity().title = getString(R.string.toolbar_title_contact_details)
    }
}
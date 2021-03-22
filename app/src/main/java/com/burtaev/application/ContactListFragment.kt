package com.burtaev.application

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {
    companion object {
        fun newInstance() = ContactListFragment()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ivPhoto = view.findViewById<ImageView>(R.id.contact_photo)
        val tvName = view.findViewById<TextView>(R.id.contact_name)
        val tvPhone = view.findViewById<TextView>(R.id.contact_phone)
        val contactsDataSource: ContactsDataSource = ContactModel()
        val contactList: List<Contact> = contactsDataSource.getAllContact()
        val contact: Contact = contactList.random()
        if (contact != null) {
        ivPhoto.setImageResource(contact.img)
        tvName.text = contact.name
        tvPhone.text = contact.phoneNum
            view.setOnClickListener {
                (requireActivity() as? OnContactClickedListener)?.showContactDetailsFragment(contact.id)
            }
        }
        requireActivity().title = getString(R.string.toolbar_title_contact_list)
    }
}
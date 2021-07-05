package com.burtaev.application.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.burtaev.application.OnContactClickedListener
import com.burtaev.application.R
import com.burtaev.application.model.Contact
import com.burtaev.application.viewModels.ContactListViewModel

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {
    private var contactListViewModel: ContactListViewModel? = null

    companion object {
        fun newInstance() = ContactListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactListViewModel = ViewModelProvider(this).get(ContactListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.toolbar_title_contact_list)
        contactListViewModel?.getContactList()
            ?.observe(viewLifecycleOwner, Observer { contactList ->
                updateFields(contactList[0])
                view.setOnClickListener {
                    (requireActivity() as? OnContactClickedListener)?.showContactDetailsFragment(
                        contactList[0].id
                    )
                }
            })
    }

    private fun updateFields(contact: Contact) {
        val ivPhoto = view?.findViewById<ImageView>(R.id.contact_photo) ?: return
        val tvName = view?.findViewById<TextView>(R.id.contact_name) ?: return
        val tvPhone = view?.findViewById<TextView>(R.id.contact_phone) ?: return
        with(contact) {
            when (img != null) {
                true -> ivPhoto.setImageURI(img)
                false -> ivPhoto.setImageResource(R.drawable.default_user_icon)
            }
            tvName.text = name
            tvPhone.text = phoneNum
        }
    }
}
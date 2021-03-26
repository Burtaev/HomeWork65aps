package com.burtaev.application

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private const val ARG_PARAM_CONTACT_ID = "contactId"

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {
    private var scope: CoroutineScope? = null

    companion object {
        fun newInstance(contactId: Long) = ContactDetailsFragment().apply {
            arguments = bundleOf(ARG_PARAM_CONTACT_ID to contactId)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        scope = CoroutineScope(Dispatchers.IO)
    }

    override fun onDetach() {
        scope?.cancel()
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.toolbar_title_contact_details)
        val contactId = arguments?.getLong(ARG_PARAM_CONTACT_ID, 0) ?: 0
        scope?.launch {
            val dataFetchService = requireActivity() as? ContactsDataFetchService ?: return@launch
            while (!dataFetchService.isServiceBound()) {
            }
            val contact = dataFetchService.getContactDetails(contactId)
            requireActivity().runOnUiThread {
                updateFields(contact)
            }
        }
    }

    private fun updateFields(contact: Contact?) {
        if (contact != null) {
            val ivPhoto = view?.findViewById<ImageView>(R.id.ivContactPhoto)
            val tvName = view?.findViewById<TextView>(R.id.tvContactName)
            val tvPhone = view?.findViewById<TextView>(R.id.tvContactPhone)
            val tvPhone2 = view?.findViewById<TextView>(R.id.tvContactPhone2)
            val tvEmail = view?.findViewById<TextView>(R.id.tvContactEmail)
            val tvEmail2 = view?.findViewById<TextView>(R.id.tvContactEmail2)
            val tvDescription = view?.findViewById<TextView>(R.id.tvContactDescription)
            ivPhoto?.setImageResource(contact.img)
            tvName?.text = contact.name
            tvPhone?.text = contact.phoneNum
            tvPhone2?.text = contact.phoneNum2
            tvEmail?.text = contact.email
            tvEmail2?.text = contact.email2
            tvDescription?.text = contact.description
        }
    }
}
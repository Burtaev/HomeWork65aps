package com.burtaev.application

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class ContactListFragment : Fragment(R.layout.fragment_contact_list) {
    private var scope: CoroutineScope? = null

    companion object {
        fun newInstance() = ContactListFragment()
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
        requireActivity().title = getString(R.string.toolbar_title_contact_list)

        scope?.launch {
            val dataFetchService = requireActivity() as? ContactsDataFetchService ?: return@launch
            while (!dataFetchService.isServiceBound()) {
            }
            val contact = dataFetchService.getContactsList()?.firstOrNull() ?: return@launch
            requireActivity().runOnUiThread {
                updateFields(contact)
                view.setOnClickListener {
                    (requireActivity() as? OnContactClickedListener)?.showContactDetailsFragment(
                        contact.id
                    )
                }
            }
        }
    }

    private fun updateFields(contact: Contact?) {
        val ivPhoto = view?.findViewById<ImageView>(R.id.contact_photo)
        val tvName = view?.findViewById<TextView>(R.id.contact_name)
        val tvPhone = view?.findViewById<TextView>(R.id.contact_phone)
        ivPhoto?.setImageResource(contact?.img ?: 0)
        tvName?.text = contact?.name
        tvPhone?.text = contact?.phoneNum
    }
}
package com.burtaev.application

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

private const val ARG_PARAM_CONTACT_ID = "contactId"

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {
    private var scope: CoroutineScope? = null

    companion object {
        fun newInstance(contactId: Int) = ContactDetailsFragment().apply {
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
        val contactId = arguments?.getInt(ARG_PARAM_CONTACT_ID, 0) ?: 0
        scope?.launch {
            val dataFetchService = requireActivity() as? ContactsDataFetchService ?: return@launch
            while (!dataFetchService.isServiceBound()) {
            }
            val contact = dataFetchService.getContactDetails(contactId) ?: return@launch
            requireActivity().runOnUiThread {
                updateFields(contact)
                updateReminder(contact)
            }
        }
    }

    private fun updateReminder(contact: Contact) {
        val switchBirthdayReminder = view?.findViewById<Switch>(R.id.swBirthdayReminder) ?: return
        with(switchBirthdayReminder) {
            val birthdayReminder = BirthdayReminderNotification()
            isVisible = contact.dateBirthday != null
            isChecked =
                birthdayReminder.checkBirthdayReminder(requireContext(), contact)
            setOnCheckedChangeListener { _, isChecked ->
                val msg =
                    if (isChecked) R.string.toast_reminder_on else R.string.toast_reminder_off
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
                birthdayReminder.setBirthdayReminderEnabled(requireContext(),
                    contact,
                    isChecked)
            }
        }
    }

    private fun updateFields(contact: Contact) {
        val ivPhoto = view?.findViewById<ImageView>(R.id.ivContactPhoto) ?: return
        val tvName = view?.findViewById<TextView>(R.id.tvContactName) ?: return
        val tvPhone = view?.findViewById<TextView>(R.id.tvContactPhone) ?: return
        val tvPhone2 = view?.findViewById<TextView>(R.id.tvContactPhone2) ?: return
        val tvEmail = view?.findViewById<TextView>(R.id.tvContactEmail) ?: return
        val tvEmail2 = view?.findViewById<TextView>(R.id.tvContactEmail2) ?: return
        val tvDescription = view?.findViewById<TextView>(R.id.tvContactDescription) ?: return
        val tvBirthday = view?.findViewById<TextView>(R.id.tvContactBirthday) ?: return
        with(contact) {
            ivPhoto.setImageResource(img)
            tvName.text = name
            tvPhone.text = phoneNum
            tvPhone2.text = phoneNum2
            tvEmail.text = email
            tvEmail2.text = email2
            tvDescription.text = description
            if (dateBirthday != null) {
                tvBirthday.text =
                    SimpleDateFormat("d MMMM", Locale.getDefault()).format(dateBirthday.time)
            }
        }
    }
}
package com.burtaev.application.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.burtaev.application.BirthdayReminderNotification
import com.burtaev.application.R
import com.burtaev.application.databinding.FragmentContactDetailsBinding
import com.burtaev.application.model.Contact
import com.burtaev.application.viewModels.ContactDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private const val ARG_PARAM_CONTACT_ID = "contactId"

class ContactDetailsFragment :
    Fragment(R.layout.fragment_contact_details) {
    private val contactDetailsViewModel by viewModels<ContactDetailsViewModel>()
    private var _binding: FragmentContactDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(contactId: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(ARG_PARAM_CONTACT_ID to contactId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = getString(R.string.toolbar_title_contact_details)
        _binding = FragmentContactDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contactId = arguments?.getString(ARG_PARAM_CONTACT_ID) ?: ""
        contactDetailsViewModel.let {
            it.loadContactDetailsById(contactId)
            it.getContact().observe(viewLifecycleOwner, Observer { contact ->
                updateFields(contact)
                updateReminder(contact)
            })
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
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
        with(binding) {
            ivContactPhoto.setImageURI(contact.img)
            tvContactName.text = contact.name
            tvContactPhone.text = contact.phoneNum
            tvContactPhone2.text = contact.phoneNum2
            tvContactEmail.text = contact.email
            tvContactEmail2.text = contact.email2
            tvContactDescription.text = contact.description
            if (contact.dateBirthday != null) {
                tvContactBirthday.text =
                    SimpleDateFormat("d MMMM",
                        Locale.getDefault()).format(contact.dateBirthday.time)
            }
        }
    }
}
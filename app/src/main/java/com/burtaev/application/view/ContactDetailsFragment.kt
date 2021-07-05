package com.burtaev.application.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.burtaev.application.BirthdayReminderNotification
import com.burtaev.application.R
import com.burtaev.application.model.Contact
import com.burtaev.application.viewModels.ContactDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private const val ARG_PARAM_CONTACT_ID = "contactId"

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {
    private var contactDetailsViewModel: ContactDetailsViewModel? = null

    companion object {
        fun newInstance(contactId: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(ARG_PARAM_CONTACT_ID to contactId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactDetailsViewModel =
            ViewModelProvider(this).get(ContactDetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.toolbar_title_contact_details)
        val contactId = arguments?.getString(ARG_PARAM_CONTACT_ID) ?: ""
        contactDetailsViewModel?.let {
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
        val ivPhoto = view?.findViewById<ImageView>(R.id.ivContactPhoto) ?: return
        val tvName = view?.findViewById<TextView>(R.id.tvContactName) ?: return
        val tvPhone = view?.findViewById<TextView>(R.id.tvContactPhone) ?: return
        val tvPhone2 = view?.findViewById<TextView>(R.id.tvContactPhone2) ?: return
        val tvEmail = view?.findViewById<TextView>(R.id.tvContactEmail) ?: return
        val tvEmail2 = view?.findViewById<TextView>(R.id.tvContactEmail2) ?: return
        val tvDescription = view?.findViewById<TextView>(R.id.tvContactDescription) ?: return
        val tvBirthday = view?.findViewById<TextView>(R.id.tvContactBirthday) ?: return
        with(contact) {
            when (img != null) {
                true -> ivPhoto.setImageURI(img)
                false -> ivPhoto.setImageResource(R.drawable.default_user_icon)
            }
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
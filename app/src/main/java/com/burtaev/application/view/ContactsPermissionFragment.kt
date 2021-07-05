package com.burtaev.application.view

import android.Manifest.permission.READ_CONTACTS
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.burtaev.application.R

const val CONTACTS_PERMISSION_REQUEST = 200;

class ContactsPermissionFragment : Fragment(R.layout.fragment_contacts_permission) {
    companion object {
        fun newInstance() = ContactsPermissionFragment()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = ""
        val btPermission = view.findViewById<Button>(R.id.button_id) ?: return
        btPermission.setOnClickListener {
            requireActivity().requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS),
                CONTACTS_PERMISSION_REQUEST)
            if (!requireActivity().shouldShowRequestPermissionRationale(READ_CONTACTS))
                openAppSystemSettings();
        }
    }

    private fun openAppSystemSettings() {
        try {
            startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", context?.packageName, null)
            })
        } catch (e: Exception) {
            Toast.makeText(activity, R.string.toast_permission_text, Toast.LENGTH_SHORT).show()
        }
    }
}
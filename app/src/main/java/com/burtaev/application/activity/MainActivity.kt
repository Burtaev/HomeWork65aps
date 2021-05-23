package com.burtaev.application.activity

import android.Manifest.permission.READ_CONTACTS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.burtaev.application.BIRTHDAY_CHANNEL_ID
import com.burtaev.application.view.ContactListFragment
import com.burtaev.application.KEY_PERSON_ID
import com.burtaev.application.OnContactClickedListener
import com.burtaev.application.R
import com.burtaev.application.view.CONTACTS_PERMISSION_REQUEST
import com.burtaev.application.view.ContactDetailsFragment
import com.burtaev.application.view.ContactsPermissionFragment

class MainActivity : AppCompatActivity(), OnContactClickedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        if (savedInstanceState == null) {
            if (checkPermission(READ_CONTACTS)) showFragment() else showContactPermissionFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!checkPermission(READ_CONTACTS))
            showContactPermissionFragment()
    }

    private fun showFragment() {
        val id = intent.getStringExtra(KEY_PERSON_ID) ?: ""
        when (id == "") {
            true -> showContactListFragment()
            false -> showContactDetailsFragment(id)
        }
    }

    private fun showContactListFragment() {
        val fragment = ContactListFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun showContactDetailsFragment(contactID: String) {
        val fragment = ContactDetailsFragment.newInstance(contactID)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showContactPermissionFragment() {
        if (!checkPermission(READ_CONTACTS)) {
            val fragment = ContactsPermissionFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        } else
            showContactListFragment()
    }

    private fun createNotificationChannel() {
        if (SDK_INT < O) {
            return
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            BIRTHDAY_CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = getString(R.string.notification_channel_desc)
        notificationManager.createNotificationChannel(channel)
    }

    private fun checkPermission(permission: String) =
        if (SDK_INT >= M) (ContextCompat.checkSelfPermission(this,
            permission) == PackageManager.PERMISSION_GRANTED)
        else true

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_PERMISSION_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            showContactListFragment()
    }
}
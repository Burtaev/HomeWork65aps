package com.burtaev.application

import android.Manifest.permission.READ_CONTACTS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), OnContactClickedListener, ContactsDataFetchService {

    private var myService: DataFetchService? = null
    private var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        if (savedInstanceState == null) {
            if (checkPermission(READ_CONTACTS)) showFragment() else showContactPermissionFragment()
        }
        if (!isBound) {
            val intent = Intent(this, DataFetchService::class.java)
            bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!checkPermission(READ_CONTACTS))
            showContactPermissionFragment()
    }

    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            val binder = service as DataFetchService.MyLocalBinder
            myService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onDestroy() {
        if (isBound)
            unbindService(myConnection)
        super.onDestroy()
    }

    private fun showFragment() {
        val id = intent.getStringExtra(KEY_PERSON_ID) ?: ""
        if (id != "")
            showContactDetailsFragment(id)
        else
            showContactListFragment()
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

    override fun isServiceBound() = isBound

    override suspend fun getContactsList() = myService?.fetchContact()

    override suspend fun getContactDetails(id: String) = myService?.fetchContactDetailsById(id)

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
package com.burtaev.application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), OnContactClickedListener, ContactsDataFetchService {
    private var myService: DataFetchService? = null
    private var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        if (savedInstanceState == null) {
            showContactListFragment()
        }
        if (!isBound) {
            val intent = Intent(this, DataFetchService::class.java)
            bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            val binder = service as DataFetchService.MyLocalBinder
            myService = binder.getService()
            isBound = true
            val id = intent.getIntExtra(KEY_PERSON_ID, -1)
            if (id > 0)
                showContactDetailsFragment(id)
            else
                showContactListFragment()
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

    private fun showContactListFragment() {
        val fragment = ContactListFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit()
    }

    override fun showContactDetailsFragment(contactID: Int) {
        val fragment = ContactDetailsFragment.newInstance(contactID)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun isServiceBound() = isBound

    override suspend fun getContactsList() = myService?.fetchContact()

    override suspend fun getContactDetails(id: Int) = myService?.fetchContactDetailsById(id)

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < 26) {
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
}
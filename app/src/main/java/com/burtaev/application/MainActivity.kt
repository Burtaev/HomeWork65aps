package com.burtaev.application

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), OnContactClickedListener, ContactsDataFetchService {
    private var myService: DataFetchService? = null
    private var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

    override fun showContactDetailsFragment(contactID: Long) {
        val fragment = ContactDetailsFragment.newInstance(contactID)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun isServiceBound() = isBound

    override suspend fun getContactsList() = myService?.fetchContact()

    override suspend fun getContactDetails(id: Long) = myService?.fetchContactDetailsById(id)
}
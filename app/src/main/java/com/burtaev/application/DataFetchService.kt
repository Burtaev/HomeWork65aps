package com.burtaev.application

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

class DataFetchService : Service() {
    private val mBinder: IBinder = MyLocalBinder()
    private val contactsDataSource: ContactsDataSource = DataSource()

    inner class MyLocalBinder : Binder() {
        fun getService() = this@DataFetchService
    }

    override fun onBind(intent: Intent) = mBinder

    suspend fun fetchContact(): List<Contact> = withContext(
        coroutineContext
    ) {
        return@withContext contactsDataSource.getAllContact()
    }

    suspend fun fetchContactDetailsById(id: Long): Contact? = withContext(
        coroutineContext
    ) {
        return@withContext contactsDataSource.getContactById(id)
    }
}
package com.burtaev.application

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class DataFetchService() : Service() {
    private val mBinder: IBinder = MyLocalBinder()
    private val dataSource = DataSource()

    inner class MyLocalBinder : Binder() {
        fun getService() = this@DataFetchService
    }

    override fun onBind(intent: Intent) = mBinder

    suspend fun fetchContact(): List<Contact> = withContext(
        coroutineContext
    ) {
        return@withContext dataSource.getAllContact(applicationContext)
    }

    suspend fun fetchContactDetailsById(id: String): Contact? = withContext(
        coroutineContext
    ) {
        return@withContext dataSource.getContactById(id)
    }
}
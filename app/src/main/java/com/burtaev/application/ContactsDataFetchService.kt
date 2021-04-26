package com.burtaev.application

interface ContactsDataFetchService {
    fun isServiceBound(): Boolean
    suspend fun getContactsList(): List<Contact>?
    suspend fun getContactDetails(id: Int): Contact?
}
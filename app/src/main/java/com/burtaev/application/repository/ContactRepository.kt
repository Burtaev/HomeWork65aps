package com.burtaev.application.repository

import com.burtaev.application.model.Contact

interface ContactRepository {
    fun getContactsList(query: String?): List<Contact>?
    fun getContactDetails(id: String): Contact?
}
package com.burtaev.application

interface ContactsDataSource {
    fun getAllContact(): List<Contact>
    fun getContactById(id: Long): Contact?
}
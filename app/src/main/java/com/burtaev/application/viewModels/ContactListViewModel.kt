package com.burtaev.application.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.burtaev.application.model.Contact
import com.burtaev.application.repository.ContactRepository
import com.burtaev.application.repository.DataSource
import kotlinx.coroutines.launch

class ContactListViewModel(application: Application) : AndroidViewModel(application) {
    private val contactRepository: ContactRepository = DataSource(application)
    private val contactList: MutableLiveData<List<Contact>> = MutableLiveData()

    fun getContactList(): LiveData<List<Contact>> = contactList

    init {
        loadContactList()
    }

    private fun loadContactList() {
        viewModelScope.launch {
            contactList.postValue(contactRepository.getContactsList())
        }
    }
}
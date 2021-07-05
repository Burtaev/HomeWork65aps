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

class ContactDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val contactRepository: ContactRepository = DataSource(getApplication())
    private val contact = MutableLiveData<Contact>()

    fun getContact(): LiveData<Contact> = contact

    fun loadContactDetailsById(id: String) {
        viewModelScope.launch {
            contact.postValue(contactRepository.getContactDetails(id))
        }
    }
}
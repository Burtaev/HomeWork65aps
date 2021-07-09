package com.burtaev.application.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.burtaev.application.model.Contact
import com.burtaev.application.repository.ContactRepository
import com.burtaev.application.repository.DataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SEARCH_DELAY_MS = 500L

class ContactListViewModel(application: Application) : AndroidViewModel(application) {
    private val contactRepository: ContactRepository = DataSource(application)
    val contactListLiveData: MutableLiveData<List<Contact>> = MutableLiveData()
    var searchFor = ""
    var debounceJob: Job? = null
    fun getContactList(): LiveData<List<Contact>> = contactListLiveData

    fun loadContactList(query: String?) {
        viewModelScope.launch {
            contactListLiveData.postValue(contactRepository.getContactsList(query))
        }
    }

    fun searchContact(query: String?) {
        val searchText = query.toString().trim()
        if (searchText != searchFor) {
            searchFor = searchText
            debounceJob?.cancel()
            debounceJob = CoroutineScope(Dispatchers.IO).launch {
                delay(SEARCH_DELAY_MS)
                if (searchText == searchFor) {
                    contactListLiveData.postValue(contactRepository.getContactsList(query))
                }
            }
        }
    }
}


package com.burtaev.application.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.burtaev.application.ItemsDecoration
import com.burtaev.application.OnContactClickedListener
import com.burtaev.application.R
import com.burtaev.application.activity.MainActivity
import com.burtaev.application.adapter.ContactAdapter
import com.burtaev.application.databinding.FragmentContactListBinding
import com.burtaev.application.viewModels.ContactListViewModel

class ContactListFragment :
    Fragment(R.layout.fragment_contact_list) {
    private val contactListViewModel by viewModels<ContactListViewModel>()
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ContactListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = getString(R.string.toolbar_title_contact_list)
        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        initContactListRv()
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initContactListRv() {
        contactListViewModel.loadContactList(null)
        contactListViewModel.getContactList()
        val adapter = ContactAdapter { contactID ->
            (requireActivity() as? OnContactClickedListener)?.showContactDetailsFragment(
                contactID
            )
        }
        with(binding) {
            rvContactList.adapter = adapter
            rvContactList.addItemDecoration(ItemsDecoration())
        }
        contactListViewModel.contactListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_list_search, menu)
        val searchView =
            SearchView((context as MainActivity).supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.action_search).apply {
            actionView = searchView
        }
        searchView.queryHint = getString(R.string.title_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
              searchContacts(newText)
                return true
            }
        })
    }

    private fun searchContacts(query: String) {
        contactListViewModel.searchContact(query)
    }
}
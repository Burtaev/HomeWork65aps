package com.burtaev.application.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.burtaev.application.model.Contact

class ContactAdapter(private val itemOnClick: (String) -> Unit) :
    ListAdapter<Contact, ContactViewHolder>(ContactDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder = ContactViewHolder.from(parent)

    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int
    ) = holder.bind(itemOnClick, getItem(position))
}
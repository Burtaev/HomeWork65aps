package com.burtaev.application.adapter

import androidx.recyclerview.widget.DiffUtil
import com.burtaev.application.model.Contact

class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact) =
        oldItem == newItem
}
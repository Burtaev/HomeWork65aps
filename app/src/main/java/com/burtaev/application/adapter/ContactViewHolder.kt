package com.burtaev.application.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.burtaev.application.databinding.FragmentListContactItemBinding
import com.burtaev.application.model.Contact

class ContactViewHolder private constructor(
    private val binding: FragmentListContactItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(itemOnClick: (String) -> Unit, item: Contact) {
        with(binding) {
            root.setOnClickListener {
                itemOnClick(item.id)
            }
            contactPhoto.setImageURI(item.img)
            contactName.text = item.name
            contactPhone.text = item.phoneNum
        }
    }

    companion object {
        fun from(parent: ViewGroup): ContactViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = FragmentListContactItemBinding.inflate(layoutInflater, parent, false)
            return ContactViewHolder(binding)
        }
    }
}
package com.spaceandjonin.mycrd.adapter.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.spaceandjonin.mycrd.models.EmailAddress

class EmailAddressTypeDiffCallback : DiffUtil.ItemCallback<EmailAddress.EmailType>() {
    override fun areItemsTheSame(oldItem: EmailAddress.EmailType, newItem: EmailAddress.EmailType): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: EmailAddress.EmailType, newItem: EmailAddress.EmailType): Boolean {
        return oldItem == newItem
    }
}
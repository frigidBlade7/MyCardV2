package com.spaceandjonin.mycrd.adapter.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.spaceandjonin.mycrd.models.EmailAddress

class EmailAddressDiffCallback : DiffUtil.ItemCallback<EmailAddress>() {
    override fun areItemsTheSame(oldItem: EmailAddress, newItem: EmailAddress): Boolean {
        return oldItem.address == newItem.address
    }

    override fun areContentsTheSame(oldItem: EmailAddress, newItem: EmailAddress): Boolean {
        return oldItem == newItem
    }
}
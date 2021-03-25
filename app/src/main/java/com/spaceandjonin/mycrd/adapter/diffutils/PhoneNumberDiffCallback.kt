package com.spaceandjonin.mycrd.adapter.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.spaceandjonin.mycrd.models.PhoneNumber

class PhoneNumberDiffCallback : DiffUtil.ItemCallback<PhoneNumber>() {
    override fun areItemsTheSame(oldItem: PhoneNumber, newItem: PhoneNumber): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: PhoneNumber, newItem: PhoneNumber): Boolean {
        return oldItem == newItem
    }
}
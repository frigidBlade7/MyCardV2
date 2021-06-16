package com.spaceandjonin.mycrd.adapter.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.spaceandjonin.mycrd.models.PhoneNumber

class PhoneNumberTypeDiffCallback : DiffUtil.ItemCallback<PhoneNumber.PhoneNumberType>() {
    override fun areItemsTheSame(oldItem: PhoneNumber.PhoneNumberType, newItem: PhoneNumber.PhoneNumberType): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: PhoneNumber.PhoneNumberType, newItem: PhoneNumber.PhoneNumberType): Boolean {
        return oldItem == newItem
    }
}
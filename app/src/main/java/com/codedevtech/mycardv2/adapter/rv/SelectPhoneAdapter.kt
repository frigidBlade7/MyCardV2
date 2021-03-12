package com.codedevtech.mycardv2.adapter.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.adapter.diffutils.EmailAddressDiffCallback
import com.codedevtech.mycardv2.adapter.diffutils.PhoneNumberDiffCallback
import com.codedevtech.mycardv2.databinding.EmailItemBinding
import com.codedevtech.mycardv2.databinding.SelectEmailItemBinding
import com.codedevtech.mycardv2.databinding.SelectPhoneItemBinding
import com.codedevtech.mycardv2.listeners.EmailItemInteraction
import com.codedevtech.mycardv2.listeners.ItemInteraction
import com.codedevtech.mycardv2.models.EmailAddress
import com.codedevtech.mycardv2.models.PhoneNumber
import com.codedevtech.mycardv2.viewholders.BaseViewHolder

class SelectPhoneAdapter (val itemInteraction: ItemInteraction<PhoneNumber>): ListAdapter<PhoneNumber, BaseViewHolder>(PhoneNumberDiffCallback()) {

    lateinit var binding: SelectPhoneItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = SelectPhoneItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val baseViewHolder = BaseViewHolder(binding)

        binding.root.setOnClickListener{
            itemInteraction.onItemClicked(getItem(baseViewHolder.bindingAdapterPosition))
        }
        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


}
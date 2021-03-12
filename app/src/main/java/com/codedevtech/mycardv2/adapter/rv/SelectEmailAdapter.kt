package com.codedevtech.mycardv2.adapter.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.adapter.diffutils.EmailAddressDiffCallback
import com.codedevtech.mycardv2.databinding.EmailItemBinding
import com.codedevtech.mycardv2.databinding.SelectEmailItemBinding
import com.codedevtech.mycardv2.listeners.EmailItemInteraction
import com.codedevtech.mycardv2.listeners.ItemInteraction
import com.codedevtech.mycardv2.models.EmailAddress
import com.codedevtech.mycardv2.viewholders.BaseViewHolder

class SelectEmailAdapter (val itemInteraction: ItemInteraction<EmailAddress>): ListAdapter<EmailAddress, BaseViewHolder>(EmailAddressDiffCallback()) {

    lateinit var binding: SelectEmailItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = SelectEmailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
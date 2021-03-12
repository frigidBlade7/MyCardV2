package com.codedevtech.mycardv2.adapter.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.adapter.diffutils.EmailAddressDiffCallback
import com.codedevtech.mycardv2.databinding.EmailItemBinding
import com.codedevtech.mycardv2.listeners.EmailItemInteraction
import com.codedevtech.mycardv2.models.EmailAddress
import com.codedevtech.mycardv2.viewholders.BaseViewHolder

class EmailAdapter (val itemInteraction: EmailItemInteraction, val arrayAdapter: ArrayAdapter<String>): ListAdapter<EmailAddress, BaseViewHolder>(EmailAddressDiffCallback()) {

    lateinit var binding: EmailItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = EmailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val baseViewHolder = BaseViewHolder(binding)

        binding.type.setAdapter(arrayAdapter)
        binding.remove.setOnClickListener{
            if(itemCount>1)
                itemInteraction.onItemClicked(getItem(baseViewHolder.bindingAdapterPosition))
        }

        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))

        if(position==0){
            binding.remove.visibility = View.INVISIBLE
        }else
            binding.remove.visibility = View.VISIBLE
    }


}
package com.codedevtech.mycardv2.adapter.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.databinding.EmailItemBinding
import com.codedevtech.mycardv2.databinding.PhoneItemBinding
import com.codedevtech.mycardv2.fragments.dashboard.AddPersonalCardFragment
import com.codedevtech.mycardv2.listeners.EmailItemInteraction
import com.codedevtech.mycardv2.listeners.ItemInteraction
import com.codedevtech.mycardv2.models.EmailAddress
import com.codedevtech.mycardv2.models.PhoneNumber
import com.codedevtech.mycardv2.viewholders.BaseViewHolder

class EmailAdapter (val itemInteraction: EmailItemInteraction, val arrayAdapter: ArrayAdapter<String>): ListAdapter<EmailAddress, BaseViewHolder>(DiffCallback()) {

    lateinit var binding: EmailItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = EmailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val baseViewHolder = BaseViewHolder(binding)

        binding.type.setAdapter(arrayAdapter)
        binding.remove.setOnClickListener{
            if(itemCount>1)
                itemInteraction.onItemClicked(getItem(baseViewHolder.adapterPosition))
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


    private class DiffCallback : DiffUtil.ItemCallback<EmailAddress>() {
        override fun areItemsTheSame(oldItem: EmailAddress, newItem: EmailAddress): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: EmailAddress, newItem: EmailAddress): Boolean {
            return oldItem == newItem
        }
    }

    fun removeItem(item: EmailAddress){
        val mutable = mutableListOf<EmailAddress>()
        mutable.addAll(currentList)
        mutable.remove(item)
        submitList(mutable.sortedBy { it.id })
    }

    fun addItem(){
        val mutable = mutableListOf<EmailAddress>()
        mutable.addAll(currentList)
        mutable.add(EmailAddress())
        submitList(mutable.sortedBy { it.id })
    }


}
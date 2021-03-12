package com.codedevtech.mycardv2.adapter.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.adapter.diffutils.PhoneNumberDiffCallback
import com.codedevtech.mycardv2.databinding.PhoneItemBinding
import com.codedevtech.mycardv2.listeners.ItemInteraction
import com.codedevtech.mycardv2.models.PhoneNumber
import com.codedevtech.mycardv2.viewholders.BaseViewHolder

class PhoneNumberAdapter (val itemInteraction: ItemInteraction<PhoneNumber>,val arrayAdapter: ArrayAdapter<String>): ListAdapter<PhoneNumber, BaseViewHolder>(PhoneNumberDiffCallback()) {

    lateinit var binding: PhoneItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = PhoneItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
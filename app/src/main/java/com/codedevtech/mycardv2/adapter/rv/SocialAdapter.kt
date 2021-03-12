package com.codedevtech.mycardv2.adapter.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.adapter.diffutils.SocialDiffCallback
import com.codedevtech.mycardv2.databinding.SocialItemBinding
import com.codedevtech.mycardv2.listeners.SocialItemInteraction
import com.codedevtech.mycardv2.models.SocialMediaProfile
import com.codedevtech.mycardv2.viewholders.BaseViewHolder

class SocialAdapter (val itemInteraction: SocialItemInteraction?): ListAdapter<SocialMediaProfile, BaseViewHolder>(
    SocialDiffCallback()
) {

    lateinit var binding: SocialItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        binding = SocialItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (itemInteraction==null)
            binding.edit.visibility = View.GONE

        val baseViewHolder = BaseViewHolder(binding)

        binding.title.setOnClickListener{
            itemInteraction?.onItemClicked(getItem(baseViewHolder.bindingAdapterPosition))
        }

        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }




}
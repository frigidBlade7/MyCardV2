package com.codedevtech.mycardv2.adapter.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.databinding.EmailItemBinding
import com.codedevtech.mycardv2.databinding.PhoneItemBinding
import com.codedevtech.mycardv2.databinding.SocialItemBinding
import com.codedevtech.mycardv2.fragments.dashboard.AddPersonalCardFragment
import com.codedevtech.mycardv2.listeners.EmailItemInteraction
import com.codedevtech.mycardv2.listeners.ItemInteraction
import com.codedevtech.mycardv2.listeners.SocialItemInteraction
import com.codedevtech.mycardv2.models.EmailAddress
import com.codedevtech.mycardv2.models.PhoneNumber
import com.codedevtech.mycardv2.models.SocialMediaProfile
import com.codedevtech.mycardv2.viewholders.BaseViewHolder

class SocialAdapter (val itemInteraction: SocialItemInteraction): ListAdapter<SocialMediaProfile, BaseViewHolder>(DiffCallback()) {

    lateinit var binding: SocialItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = SocialItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val baseViewHolder = BaseViewHolder(binding)

        binding.title.setOnClickListener{
            if(itemCount>1)
                itemInteraction.onItemClicked(getItem(baseViewHolder.adapterPosition))
        }

        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


    private class DiffCallback : DiffUtil.ItemCallback<SocialMediaProfile>() {
        override fun areItemsTheSame(oldItem: SocialMediaProfile, newItem: SocialMediaProfile): Boolean {
            return oldItem.usernameOrUrl == newItem.usernameOrUrl
        }

        override fun areContentsTheSame(oldItem: SocialMediaProfile, newItem: SocialMediaProfile): Boolean {
            return oldItem == newItem
        }
    }


}
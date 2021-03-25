package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.SocialDiffCallback
import com.spaceandjonin.mycrd.databinding.SocialItemBinding
import com.spaceandjonin.mycrd.listeners.SocialItemInteraction
import com.spaceandjonin.mycrd.models.SocialMediaProfile
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

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
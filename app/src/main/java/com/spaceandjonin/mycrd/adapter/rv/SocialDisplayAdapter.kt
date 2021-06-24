package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.SocialDisplayDiffCallback
import com.spaceandjonin.mycrd.databinding.SocialDisplayItemBinding
import com.spaceandjonin.mycrd.listeners.ItemInteraction
import com.spaceandjonin.mycrd.models.SocialMediaProfile
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

class SocialDisplayAdapter(val itemInteraction: ItemInteraction<SocialMediaProfile.SocialMedia>) :
    ListAdapter<SocialMediaProfile.SocialMedia, BaseViewHolder>(
        SocialDisplayDiffCallback()
    ) {

    lateinit var binding: SocialDisplayItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        binding =
            SocialDisplayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val baseViewHolder = BaseViewHolder(binding)

        binding.title.setOnClickListener {
            itemInteraction.onItemClicked(getItem(baseViewHolder.bindingAdapterPosition))
        }

        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


}
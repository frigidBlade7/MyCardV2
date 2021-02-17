package com.codedevtech.mycardv2.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.adapter.diffutils.PersonalCardDiffUtil
import com.codedevtech.mycardv2.databinding.CardListItemBinding
import com.codedevtech.mycardv2.listeners.ItemViewInteraction
import com.codedevtech.mycardv2.models.LiveCard
import com.codedevtech.mycardv2.viewholders.BaseViewHolder

class PersonalCardListAdapter(val itemInteraction: ItemViewInteraction<LiveCard?>): ListAdapter<LiveCard, BaseViewHolder>(PersonalCardDiffUtil()) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position), position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = CardListItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        val baseViewHolder = BaseViewHolder(binding)
        binding.cardItem.setOnClickListener {
            itemInteraction.onItemClicked(getItem(baseViewHolder.bindingAdapterPosition),baseViewHolder.itemView, baseViewHolder.bindingAdapterPosition)

        }
        return baseViewHolder
    }
}
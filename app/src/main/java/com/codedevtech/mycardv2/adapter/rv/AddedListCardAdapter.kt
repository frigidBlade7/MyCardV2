package com.codedevtech.mycardv2.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.adapter.diffutils.AddedCardDiffUtil
import com.codedevtech.mycardv2.databinding.AddedCardListItemBinding
import com.codedevtech.mycardv2.listeners.ItemViewInteraction
import com.codedevtech.mycardv2.models.AddedCard
import com.codedevtech.mycardv2.viewholders.BaseViewHolder

class AddedListCardAdapter(val itemInteraction: ItemViewInteraction<AddedCard?>): ListAdapter<AddedCard, BaseViewHolder>(AddedCardDiffUtil()) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position), position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = AddedCardListItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        val baseViewHolder = BaseViewHolder(binding)
        binding.cardItem.setOnClickListener {
            itemInteraction.onItemClicked(getItem(baseViewHolder.bindingAdapterPosition),baseViewHolder.itemView, baseViewHolder.bindingAdapterPosition)

        }
        return baseViewHolder
    }
}
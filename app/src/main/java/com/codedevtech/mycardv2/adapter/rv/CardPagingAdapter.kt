package com.codedevtech.mycardv2.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.codedevtech.mycardv2.adapter.diffutils.CardDiffUtil
import com.codedevtech.mycardv2.databinding.CardItemBinding
import com.codedevtech.mycardv2.databinding.CardListItemBinding
import com.codedevtech.mycardv2.listeners.ItemInteraction
import com.codedevtech.mycardv2.listeners.ItemViewInteraction
import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.viewholders.BaseViewHolder

class CardPagingAdapter(val itemInteraction: ItemViewInteraction<Card?>): PagingDataAdapter<Card, BaseViewHolder>(CardDiffUtil()) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = CardListItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        val baseViewHolder = BaseViewHolder(binding)
        binding.cardItem.setOnClickListener {
            itemInteraction.onItemClicked(getItem(baseViewHolder.bindingAdapterPosition),baseViewHolder.itemView)

        }
        return baseViewHolder
    }
}
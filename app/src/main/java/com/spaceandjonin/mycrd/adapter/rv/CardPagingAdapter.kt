package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.AddedCardDiffUtil
import com.spaceandjonin.mycrd.databinding.AddedCardListItemBinding
import com.spaceandjonin.mycrd.listeners.ItemViewInteraction
import com.spaceandjonin.mycrd.models.AddedCard
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

class CardPagingAdapter(val itemInteraction: ItemViewInteraction<AddedCard?>) :
    PagingDataAdapter<AddedCard, BaseViewHolder>(
        AddedCardDiffUtil()
    ) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position), position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            AddedCardListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val baseViewHolder = BaseViewHolder(binding)
        binding.cardItem.setOnClickListener {
            binding.icon.visibility = View.INVISIBLE //temp fix for black background
            itemInteraction.onItemClicked(
                getItem(baseViewHolder.bindingAdapterPosition),
                baseViewHolder.itemView,
                baseViewHolder.bindingAdapterPosition
            )
        }
        return baseViewHolder
    }
}
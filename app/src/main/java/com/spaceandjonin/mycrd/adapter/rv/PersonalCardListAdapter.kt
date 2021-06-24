package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.PersonalCardDiffUtil
import com.spaceandjonin.mycrd.databinding.CardListItemBinding
import com.spaceandjonin.mycrd.listeners.ItemViewInteraction
import com.spaceandjonin.mycrd.models.LiveCard
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

class PersonalCardListAdapter(val itemInteraction: ItemViewInteraction<LiveCard?>) :
    ListAdapter<LiveCard, BaseViewHolder>(PersonalCardDiffUtil()) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position), position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            CardListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val baseViewHolder = BaseViewHolder(binding)
        binding.cardItem.setOnClickListener {
            itemInteraction.onItemClicked(
                getItem(baseViewHolder.bindingAdapterPosition),
                baseViewHolder.itemView,
                baseViewHolder.bindingAdapterPosition
            )

        }
        return baseViewHolder
    }
}
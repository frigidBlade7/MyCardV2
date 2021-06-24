package com.spaceandjonin.mycrd.adapter.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.spaceandjonin.mycrd.models.AddedCard

class AddedCardDiffUtil : DiffUtil.ItemCallback<AddedCard>() {

    override fun areItemsTheSame(oldItem: AddedCard, newItem: AddedCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AddedCard, newItem: AddedCard): Boolean {
        return oldItem == newItem
    }

}
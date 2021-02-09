package com.codedevtech.mycardv2.adapter.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.codedevtech.mycardv2.models.Card

class CardDiffUtil: DiffUtil.ItemCallback<Card>() {

    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem==newItem
    }

}
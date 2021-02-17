package com.codedevtech.mycardv2.adapter.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.codedevtech.mycardv2.models.LiveCard

class PersonalCardDiffUtil: DiffUtil.ItemCallback<LiveCard>() {

    override fun areItemsTheSame(oldItem: LiveCard, newItem: LiveCard): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: LiveCard, newItem: LiveCard): Boolean {
        return oldItem==newItem
    }

}
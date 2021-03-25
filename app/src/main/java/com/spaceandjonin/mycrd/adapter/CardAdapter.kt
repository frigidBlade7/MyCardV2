package com.spaceandjonin.mycrd.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.PersonalCardDiffUtil
import com.spaceandjonin.mycrd.databinding.CardItemBinding
import com.spaceandjonin.mycrd.databinding.CardItemFlippedBinding
import com.spaceandjonin.mycrd.models.LiveCard
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder


class CardAdapter : ListAdapter<LiveCard, BaseViewHolder>(PersonalCardDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        return when(viewType) {
            0-> BaseViewHolder(CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> BaseViewHolder(CardItemFlippedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

}
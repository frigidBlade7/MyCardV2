package com.codedevtech.mycardv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.adapter.diffutils.CardDiffUtil
import com.codedevtech.mycardv2.databinding.CardItemBinding
import com.codedevtech.mycardv2.databinding.CardItemFlippedBinding
import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.viewholders.BaseViewHolder


class CardAdapter : ListAdapter<Card, BaseViewHolder>(CardDiffUtil()) {
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
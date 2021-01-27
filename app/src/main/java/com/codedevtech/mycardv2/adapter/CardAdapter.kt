package com.codedevtech.mycardv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codedevtech.mycardv2.databinding.CardItemBinding
import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.viewholders.BaseViewHolder


class CardAdapter : ListAdapter<Card, BaseViewHolder>(CardDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


    class CardDiffUtil: DiffUtil.ItemCallback<Card>() {

        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem==newItem
        }

    }
}
package com.codedevtech.mycardv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.adapter.diffutils.AddedCardDiffUtil
import com.codedevtech.mycardv2.databinding.AddedCardItemBinding
import com.codedevtech.mycardv2.models.AddedCard
import com.codedevtech.mycardv2.viewholders.BaseViewHolder


class AddedCardAdapter : ListAdapter<AddedCard, BaseViewHolder>(AddedCardDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(AddedCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun getImage(){

    }
/*
    override fun getItemId(position: Int): Long {
        return currentList[position].hashCode().toLong()
    }
*/

}
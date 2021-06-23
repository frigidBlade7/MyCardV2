package com.spaceandjonin.mycrd.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.AddedCardDiffUtil
import com.spaceandjonin.mycrd.databinding.AddedCardItemBinding
import com.spaceandjonin.mycrd.models.AddedCard
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder


class AddedCardAdapter : ListAdapter<AddedCard, BaseViewHolder>(AddedCardDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            AddedCardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun getImage() {

    }
/*
    override fun getItemId(position: Int): Long {
        return currentList[position].hashCode().toLong()
    }
*/

}
package com.spaceandjonin.mycrd.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.PersonalCardDiffUtil
import com.spaceandjonin.mycrd.databinding.LiveCardItemBinding
import com.spaceandjonin.mycrd.models.LiveCard
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder


class PersonalCardAdapter : ListAdapter<LiveCard, BaseViewHolder>(PersonalCardDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            LiveCardItemBinding.inflate(
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

/*
    override fun getItemId(position: Int): Long {
        return currentList[position].hashCode().toLong()
    }
*/

}
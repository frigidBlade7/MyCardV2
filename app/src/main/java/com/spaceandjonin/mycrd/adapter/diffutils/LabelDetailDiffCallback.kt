package com.spaceandjonin.mycrd.adapter.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.spaceandjonin.mycrd.models.LabelDetail

class LabelDetailDiffCallback : DiffUtil.ItemCallback<LabelDetail>() {
    override fun areItemsTheSame(oldItem: LabelDetail, newItem: LabelDetail): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: LabelDetail, newItem: LabelDetail): Boolean {
        return oldItem == newItem
    }
}
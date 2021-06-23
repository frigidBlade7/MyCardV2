package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.StringDiffCallback
import com.spaceandjonin.mycrd.databinding.LabelChildItemBinding
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder


class ChildAdapter : ListAdapter<String, BaseViewHolder>(StringDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            LabelChildItemBinding.inflate(
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

}
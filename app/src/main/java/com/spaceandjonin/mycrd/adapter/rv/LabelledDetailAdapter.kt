package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.LabelDetailDiffCallback
import com.spaceandjonin.mycrd.databinding.LabelledItemBinding
import com.spaceandjonin.mycrd.listeners.LabelledDetailItemInteraction
import com.spaceandjonin.mycrd.models.LabelDetail
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

class LabelledDetailAdapter (val itemInteraction: LabelledDetailItemInteraction):
    ListAdapter<LabelDetail, BaseViewHolder>(LabelDetailDiffCallback()) {

    lateinit var binding: LabelledItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = LabelledItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val baseViewHolder = BaseViewHolder(binding)

        binding.actions.edit.setOnClickListener{
            itemInteraction.onEditClicked(getItem(baseViewHolder.bindingAdapterPosition))
        }

        binding.actions.untag.setOnClickListener {
            itemInteraction.onRemoveLabelClicked(getItem(baseViewHolder.bindingAdapterPosition))
        }
        binding.actions.swap.setOnClickListener{
            itemInteraction.onSwapClicked(getItem(baseViewHolder.bindingAdapterPosition))

        }

        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val labelDetail = getItem(position)
        val labelList = currentList.filter {it.label==labelDetail.label}
        holder.bindTo(labelDetail, labelList.indexOf(labelDetail))
    }


}
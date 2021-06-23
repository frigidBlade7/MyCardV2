package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.TooltipCompat
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.diffutils.StringDiffCallback
import com.spaceandjonin.mycrd.databinding.UnlabelledItemBinding
import com.spaceandjonin.mycrd.listeners.UnlabelledDetailItemInteraction
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

class UnlabelledStringsAdapter(val itemInteraction: UnlabelledDetailItemInteraction) :
    ListAdapter<String, BaseViewHolder>(StringDiffCallback()) {

    lateinit var binding: UnlabelledItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = UnlabelledItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val baseViewHolder = BaseViewHolder(binding)

        TooltipCompat.setTooltipText(binding.copy, parent.context.getString(R.string.copy))
        TooltipCompat.setTooltipText(binding.remove, parent.context.getString(R.string.delete))
        TooltipCompat.setTooltipText(
            binding.addLabel,
            parent.context.getString(R.string.assign_label)
        )


        binding.remove.setOnClickListener {
            itemInteraction.onRemoveClicked(getItem(baseViewHolder.bindingAdapterPosition))
        }

        binding.data.setOnClickListener {
            itemInteraction.onItemClicked(getItem(baseViewHolder.bindingAdapterPosition))
        }
        binding.copy.setOnClickListener {
            itemInteraction.onCopyClicked(getItem(baseViewHolder.bindingAdapterPosition))

        }
        binding.addLabel.setOnClickListener {
            itemInteraction.onTagClicked(getItem(baseViewHolder.bindingAdapterPosition))
        }

        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


}
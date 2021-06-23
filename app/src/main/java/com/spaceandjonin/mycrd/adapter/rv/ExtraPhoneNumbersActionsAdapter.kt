package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.TooltipCompat
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.diffutils.PhoneNumberDiffCallback
import com.spaceandjonin.mycrd.databinding.CardPhoneItemAdditionalActionsBinding
import com.spaceandjonin.mycrd.listeners.LabelledDetailItemInteraction
import com.spaceandjonin.mycrd.models.LabelDetail
import com.spaceandjonin.mycrd.models.PhoneNumber
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

class ExtraPhoneNumbersActionsAdapter(val itemInteraction: LabelledDetailItemInteraction) :
    ListAdapter<PhoneNumber, BaseViewHolder>(PhoneNumberDiffCallback()) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = CardPhoneItemAdditionalActionsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val baseViewHolder = BaseViewHolder(binding)

        binding.actions.edit.setOnClickListener {
            val phoneNumber = getItem(baseViewHolder.bindingAdapterPosition)
            TooltipCompat.setTooltipText(it, parent.context.getString(R.string.edit))
            itemInteraction.onEditClicked(LabelDetail(phoneNumber.type.name, phoneNumber.number))
        }
        binding.actions.swap.setOnClickListener {
            val phoneNumber = getItem(baseViewHolder.bindingAdapterPosition)
            TooltipCompat.setTooltipText(it, parent.context.getString(R.string.change_label))

            itemInteraction.onSwapClicked(LabelDetail(phoneNumber.type.name, phoneNumber.number))
        }
        binding.actions.untag.setOnClickListener {
            val phoneNumber = getItem(baseViewHolder.bindingAdapterPosition)
            TooltipCompat.setTooltipText(it, parent.context.getString(R.string.remove_label))

            itemInteraction.onRemoveLabelClicked(
                LabelDetail(
                    phoneNumber.type.name,
                    phoneNumber.number
                )
            )
        }
        return baseViewHolder
    }
}
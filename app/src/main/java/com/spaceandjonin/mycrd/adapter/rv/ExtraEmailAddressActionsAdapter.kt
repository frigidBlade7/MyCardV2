package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.TooltipCompat
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.diffutils.EmailAddressDiffCallback
import com.spaceandjonin.mycrd.databinding.CardEmailItemAdditionalActionsBinding
import com.spaceandjonin.mycrd.listeners.LabelledDetailItemInteraction
import com.spaceandjonin.mycrd.models.EmailAddress
import com.spaceandjonin.mycrd.models.LabelDetail
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

class ExtraEmailAddressActionsAdapter(val itemInteraction: LabelledDetailItemInteraction) :
    ListAdapter<EmailAddress, BaseViewHolder>(EmailAddressDiffCallback()) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = CardEmailItemAdditionalActionsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val baseViewHolder = BaseViewHolder(binding)


        binding.actions.edit.setOnClickListener {
            val emailAddress = getItem(baseViewHolder.bindingAdapterPosition)
            TooltipCompat.setTooltipText(it, parent.context.getString(R.string.edit))

            itemInteraction.onEditClicked(LabelDetail(emailAddress.type.name, emailAddress.address))
        }
        binding.actions.swap.setOnClickListener {
            val emailAddress = getItem(baseViewHolder.bindingAdapterPosition)
            TooltipCompat.setTooltipText(it, parent.context.getString(R.string.change_label))
            itemInteraction.onSwapClicked(LabelDetail(emailAddress.type.name, emailAddress.address))
        }
        binding.actions.untag.setOnClickListener {
            val emailAddress = getItem(baseViewHolder.bindingAdapterPosition)
            TooltipCompat.setTooltipText(it, parent.context.getString(R.string.remove_label))
            itemInteraction.onRemoveLabelClicked(
                LabelDetail(
                    emailAddress.type.name,
                    emailAddress.address
                )
            )
        }

        return baseViewHolder
    }
}
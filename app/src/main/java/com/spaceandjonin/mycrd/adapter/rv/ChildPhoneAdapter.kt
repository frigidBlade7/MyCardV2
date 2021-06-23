package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.PhoneNumberTypeDiffCallback
import com.spaceandjonin.mycrd.databinding.LabelChildItemBinding
import com.spaceandjonin.mycrd.listeners.PhoneTypeInteraction
import com.spaceandjonin.mycrd.models.PhoneNumber
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder


class ChildPhoneAdapter(val itemInteraction: PhoneTypeInteraction) :
    ListAdapter<PhoneNumber.PhoneNumberType, BaseViewHolder>(PhoneNumberTypeDiffCallback()) {

    lateinit var binding: LabelChildItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = LabelChildItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val baseViewHolder = BaseViewHolder(binding)
        binding.container.setOnClickListener {
            itemInteraction.onItemClicked(getItem(baseViewHolder.bindingAdapterPosition))
        }
        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position).name)
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

}
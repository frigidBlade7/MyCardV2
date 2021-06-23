package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.PhoneNumberDiffCallback
import com.spaceandjonin.mycrd.databinding.SelectPhoneItemBinding
import com.spaceandjonin.mycrd.listeners.ItemInteraction
import com.spaceandjonin.mycrd.models.PhoneNumber
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

class SelectPhoneAdapter(val itemInteraction: ItemInteraction<PhoneNumber>) :
    ListAdapter<PhoneNumber, BaseViewHolder>(PhoneNumberDiffCallback()) {

    lateinit var binding: SelectPhoneItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = SelectPhoneItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val baseViewHolder = BaseViewHolder(binding)

        binding.root.setOnClickListener {
            itemInteraction.onItemClicked(getItem(baseViewHolder.bindingAdapterPosition))
        }
        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


}
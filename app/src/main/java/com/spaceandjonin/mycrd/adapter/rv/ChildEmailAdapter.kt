package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.EmailAddressTypeDiffCallback
import com.spaceandjonin.mycrd.databinding.LabelChildItemBinding
import com.spaceandjonin.mycrd.listeners.EmailTypeInteraction
import com.spaceandjonin.mycrd.models.EmailAddress
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder


class ChildEmailAdapter(val itemInteraction: EmailTypeInteraction) :
    ListAdapter<EmailAddress.EmailType, BaseViewHolder>(EmailAddressTypeDiffCallback()) {

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

/*
    override fun getItemId(position: Int): Long {
        return currentList[position].hashCode().toLong()
    }
*/

}
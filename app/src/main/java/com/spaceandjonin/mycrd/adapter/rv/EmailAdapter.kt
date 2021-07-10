package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.EmailAddressDiffCallback
import com.spaceandjonin.mycrd.databinding.EmailItemBinding
import com.spaceandjonin.mycrd.listeners.EmailItemInteraction
import com.spaceandjonin.mycrd.models.EmailAddress
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

class EmailAdapter(
    val itemInteraction: EmailItemInteraction,
    val arrayAdapter: ArrayAdapter<String>
) : ListAdapter<EmailAddress, BaseViewHolder>(EmailAddressDiffCallback()) {

    private lateinit var binding: EmailItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = EmailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val baseViewHolder = BaseViewHolder(binding)

        binding.type.setAdapter(arrayAdapter)
        binding.remove.setOnClickListener {
            if (itemCount > 1)
                itemInteraction.onItemClicked(getItem(baseViewHolder.bindingAdapterPosition))
        }

        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))

        if (position == 0) {
            binding.remove.visibility = View.INVISIBLE
        } else
            binding.remove.visibility = View.VISIBLE
    }


}
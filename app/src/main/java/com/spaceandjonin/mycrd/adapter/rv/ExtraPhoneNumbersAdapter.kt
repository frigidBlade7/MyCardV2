package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.PhoneNumberDiffCallback
import com.spaceandjonin.mycrd.databinding.CardPhoneItemAdditionalBinding
import com.spaceandjonin.mycrd.models.PhoneNumber
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

class ExtraPhoneNumbersAdapter() :
    ListAdapter<PhoneNumber, BaseViewHolder>(PhoneNumberDiffCallback()) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = CardPhoneItemAdditionalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BaseViewHolder(binding)
    }
}
package com.codedevtech.mycardv2.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.adapter.diffutils.PhoneNumberDiffCallback
import com.codedevtech.mycardv2.databinding.CardItemBinding
import com.codedevtech.mycardv2.databinding.CardListItemBinding
import com.codedevtech.mycardv2.databinding.CardPhoneItemAdditionalBinding
import com.codedevtech.mycardv2.models.LiveCard
import com.codedevtech.mycardv2.models.PhoneNumber
import com.codedevtech.mycardv2.viewholders.BaseViewHolder

class ExtraPhoneNumbersAdapter(): ListAdapter<PhoneNumber, BaseViewHolder>(PhoneNumberDiffCallback()) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = CardPhoneItemAdditionalBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return BaseViewHolder(binding)
    }
}
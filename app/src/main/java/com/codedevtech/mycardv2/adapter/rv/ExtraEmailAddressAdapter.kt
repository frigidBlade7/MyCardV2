package com.codedevtech.mycardv2.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.codedevtech.mycardv2.adapter.diffutils.EmailAddressDiffCallback
import com.codedevtech.mycardv2.adapter.diffutils.PhoneNumberDiffCallback
import com.codedevtech.mycardv2.databinding.CardEmailItemAdditionalBinding
import com.codedevtech.mycardv2.databinding.CardItemBinding
import com.codedevtech.mycardv2.databinding.CardListItemBinding
import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.models.EmailAddress
import com.codedevtech.mycardv2.models.PhoneNumber
import com.codedevtech.mycardv2.viewholders.BaseViewHolder

class ExtraEmailAddressAdapter(): ListAdapter<EmailAddress, BaseViewHolder>(EmailAddressDiffCallback()) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = CardEmailItemAdditionalBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return BaseViewHolder(binding)
    }
}
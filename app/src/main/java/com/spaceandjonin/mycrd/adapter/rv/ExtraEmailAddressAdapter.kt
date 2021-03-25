package com.spaceandjonin.mycrd.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.spaceandjonin.mycrd.adapter.diffutils.EmailAddressDiffCallback
import com.spaceandjonin.mycrd.databinding.CardEmailItemAdditionalBinding
import com.spaceandjonin.mycrd.models.EmailAddress
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

class ExtraEmailAddressAdapter(): ListAdapter<EmailAddress, BaseViewHolder>(EmailAddressDiffCallback()) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = CardEmailItemAdditionalBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return BaseViewHolder(binding)
    }
}
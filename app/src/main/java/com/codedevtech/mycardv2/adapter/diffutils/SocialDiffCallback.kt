package com.codedevtech.mycardv2.adapter.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.codedevtech.mycardv2.models.SocialMediaProfile


class SocialDiffCallback : DiffUtil.ItemCallback<SocialMediaProfile>() {
    override fun areItemsTheSame(oldItem: SocialMediaProfile, newItem: SocialMediaProfile): Boolean {
        return oldItem.usernameOrUrl == newItem.usernameOrUrl
    }

    override fun areContentsTheSame(oldItem: SocialMediaProfile, newItem: SocialMediaProfile): Boolean {
        return oldItem == newItem
    }
}


package com.spaceandjonin.mycrd.adapter.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.spaceandjonin.mycrd.models.SocialMediaProfile


class SocialDisplayDiffCallback : DiffUtil.ItemCallback<SocialMediaProfile.SocialMedia>() {
    override fun areItemsTheSame(
        oldItem: SocialMediaProfile.SocialMedia,
        newItem: SocialMediaProfile.SocialMedia
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: SocialMediaProfile.SocialMedia,
        newItem: SocialMediaProfile.SocialMedia
    ): Boolean {
        return oldItem == newItem
    }
}


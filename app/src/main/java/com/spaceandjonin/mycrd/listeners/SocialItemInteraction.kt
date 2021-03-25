package com.spaceandjonin.mycrd.listeners

import com.spaceandjonin.mycrd.models.SocialMediaProfile


interface SocialItemInteraction {
    fun onItemClicked(item: SocialMediaProfile)
}
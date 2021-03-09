package com.codedevtech.mycardv2.listeners

import com.codedevtech.mycardv2.models.SocialMediaProfile


interface SocialItemInteraction {
    fun onItemClicked(item: SocialMediaProfile)
}
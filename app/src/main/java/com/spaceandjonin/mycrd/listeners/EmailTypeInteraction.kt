package com.spaceandjonin.mycrd.listeners

import com.spaceandjonin.mycrd.models.EmailAddress


interface EmailTypeInteraction {
    fun onItemClicked(item: EmailAddress.EmailType)

}
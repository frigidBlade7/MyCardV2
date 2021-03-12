package com.spaceandjonin.mycrd.listeners

import com.spaceandjonin.mycrd.models.EmailAddress


interface EmailItemInteraction {
    fun onItemClicked(item: EmailAddress)
}
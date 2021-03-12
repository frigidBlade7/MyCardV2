package com.codedevtech.mycardv2.listeners

import com.codedevtech.mycardv2.models.EmailAddress


interface EmailItemInteraction {
    fun onItemClicked(item: EmailAddress)
}
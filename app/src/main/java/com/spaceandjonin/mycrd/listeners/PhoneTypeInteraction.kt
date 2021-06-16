package com.spaceandjonin.mycrd.listeners

import com.spaceandjonin.mycrd.models.PhoneNumber


interface PhoneTypeInteraction {
    fun onItemClicked(item: PhoneNumber.PhoneNumberType)

}
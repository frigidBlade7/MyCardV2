package com.spaceandjonin.mycrd.converters

import androidx.databinding.InverseMethod
import com.spaceandjonin.mycrd.models.EmailAddress
import com.spaceandjonin.mycrd.models.PhoneNumber

object PhoneTypeBindingConverter {

    fun phoneTypeToString(phoneNumberType: PhoneNumber.PhoneNumberType):String{
        return phoneNumberType.toString()
    }

    @InverseMethod(value = "phoneTypeToString")
    fun stringToPhoneType(string: String):PhoneNumber.PhoneNumberType{
        return  PhoneNumber.PhoneNumberType.values().first { it.name == string }
    }

}

object EmailTypeBindingConverter {

    fun emailTypeToString(emailType: EmailAddress.EmailType):String{
        return emailType.toString()
    }

    @InverseMethod(value = "emailTypeToString")
    fun stringToEmailType(string: String):EmailAddress.EmailType{
        return  EmailAddress.EmailType.values().first { it.name == string }
    }

}
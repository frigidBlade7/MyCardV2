package com.codedevtech.mycardv2.converters

import android.content.Context
import androidx.databinding.InverseMethod
import com.codedevtech.mycardv2.models.EmailAddress
import com.codedevtech.mycardv2.models.PhoneNumber

object PhoneTypeConverter {

    fun phoneTypeToString(phoneNumberType: PhoneNumber.PhoneNumberType):String{
        return phoneNumberType.toString()
    }

    @InverseMethod(value = "phoneTypeToString")
    fun stringToPhoneType(string: String):PhoneNumber.PhoneNumberType{
        return  PhoneNumber.PhoneNumberType.values().first { it.name == string }
    }

}

object EmailTypeConverter {

    fun emailTypeToString(emailType: EmailAddress.EmailType):String{
        return emailType.toString()
    }

    @InverseMethod(value = "emailTypeToString")
    fun stringToEmailType(string: String):EmailAddress.EmailType{
        return  EmailAddress.EmailType.values().first { it.name == string }
    }

}
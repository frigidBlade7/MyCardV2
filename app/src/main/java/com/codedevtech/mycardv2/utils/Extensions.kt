package com.codedevtech.mycardv2.utils

import android.util.Log
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.models.SocialMediaProfile
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException


fun Card?.initials():String{
    this?.let {
        return name.first.substring(0,1) + name.last.substring(0,1)
    }
    return ""
}

fun List<SocialMediaProfile>.hasAtLeastOne(): Boolean{
    return this.any { it.usernameOrUrl.isNotEmpty() }
}

fun Card?.fullname():String{
    this?.let {
        return "${name.prefix} ${name.first} ${name.last}".trim()
    }
    return ""
}

fun Exception.getCode(): Int{
    return when(this){
        is FirebaseAuthInvalidCredentialsException -> {
            R.string.invalid_credentials
        }
        is FirebaseTooManyRequestsException -> {
            R.string.too_many_tries
        }
        else ->{
            Log.d("ERROR", "onVerificationFailed: ${this.localizedMessage}")
            R.string.default_error
        }
    }
}
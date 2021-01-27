package com.codedevtech.mycardv2.utils

import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.models.SocialMediaProfile


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
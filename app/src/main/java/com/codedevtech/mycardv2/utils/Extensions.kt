package com.codedevtech.mycardv2.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.models.Name
import com.codedevtech.mycardv2.models.SocialMediaProfile
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlin.random.Random


fun Card?.initials():String{
    this?.let {
        return "${name.firstName.trim().getOrNull(0)?:""}${name.lastName.trim().getOrNull(0)?:""}".trim()
    }
    return ""
}

fun View.backgroundColor(colorCode: Int): Int{
    return when(colorCode%3){
        0 -> ContextCompat.getColor(context, R.color.mc_purple_10)
        1 -> ContextCompat.getColor(context, R.color.mc_blue_20)
        2-> ContextCompat.getColor(context, R.color.mc_orange_30)
        else -> ContextCompat.getColor(context, R.color.mc_gray_light)
    }
}

fun View.initialsColor(colorCode: Int): Int{
    return when(colorCode%3){
        0 -> ContextCompat.getColor(context, R.color.mc_purple)
        1 -> ContextCompat.getColor(context, R.color.mc_blue)
        2-> ContextCompat.getColor(context, R.color.mc_orange)
        else -> ContextCompat.getColor(context, android.R.color.darker_gray)
    }
}

fun View.textColor(type: String?): Int{
    return when(type){
        this.context.getString(R.string.personal) -> ContextCompat.getColor(context, R.color.mc_purple)
        this.context.getString(R.string.mobile) -> ContextCompat.getColor(context, R.color.mc_purple)
        this.context.getString(R.string.home) -> ContextCompat.getColor(context, R.color.mc_green)
        this.context.getString(R.string.work) -> ContextCompat.getColor(context, R.color.mc_green)
        else -> ContextCompat.getColor(context, R.color.mc_orange)
    }
}

fun List<SocialMediaProfile>.hasAtLeastOne(): Boolean{
    return this.any { it.usernameOrUrl.isNotEmpty() }
}

fun Name.aggregateNameToFullName(){
    if(middleName.isNotEmpty()){
        fullName = "$firstName $middleName $lastName".trim()
        return
    }

    fullName = "$firstName $lastName".trim()

}

//just gonna use this to trigger the livedata since it behaves a little fuzzy with lists
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

fun Name.segregateFullName(){
    val list =  fullName.split(" ").toTypedArray()

    when (list.size){
        1-> firstName = list.getOrNull(0)?:""
        2-> {
            firstName = list.getOrNull(0)?:""
            lastName = list.getOrNull(1) ?:""
        }
        3->{

            firstName = list.getOrNull(0)?:""
            middleName = list.getOrNull(1) ?:""
            lastName = list.getOrNull(2) ?:""
        }
    }
}

fun Name?.fullname():String{
    this?.let {
        if(it.middleName.isNotEmpty())
            return "${it.prefix} ${it.firstName} ${it.middleName} ${it.lastName} ${it.suffix}".trim()
        return "${it.prefix} ${it.firstName} ${it.lastName} ${it.suffix}".trim()
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
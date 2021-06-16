package com.spaceandjonin.mycrd.listeners

import com.google.firebase.auth.PhoneAuthCredential

abstract class AuthenticationCallbacks<T> {

    abstract fun onCodeSent()

    abstract fun onAuthSuccess(userObject: T)

    abstract fun onAuthCredentialSent(phoneAuthCredential: PhoneAuthCredential)

    abstract fun onAuthFailure(errorCode:Int)

    abstract fun onCodeTimeout()
}
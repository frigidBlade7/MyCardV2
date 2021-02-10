package com.codedevtech.mycardv2

import com.google.firebase.auth.PhoneAuthCredential

abstract class AuthenticationCallbacks {

    abstract fun onCodeSent()

    abstract fun onAuthSuccess()

    abstract fun onAuthCredentialSent(phoneAuthCredential: PhoneAuthCredential)

    abstract fun onAuthFailure(errorCode:Int)

    abstract fun onCodeTimeout()
}
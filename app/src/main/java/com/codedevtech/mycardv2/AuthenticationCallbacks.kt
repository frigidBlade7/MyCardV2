package com.codedevtech.mycardv2

abstract class AuthenticationCallbacks {

    abstract fun onCodeSent()

    abstract fun onAuthSuccess()

    abstract fun onAuthFailure(errorCode:Int)

    abstract fun onCodeTimeout()
}
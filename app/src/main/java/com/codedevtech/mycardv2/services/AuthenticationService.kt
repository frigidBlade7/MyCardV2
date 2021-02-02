package com.codedevtech.mycardv2.services

import com.codedevtech.mycardv2.AuthenticationCallbacks

interface AuthenticationService {

    fun sendVerificationCode(phoneNumber: String, timeoutInMillis: Long)

    suspend fun attemptAuth(phoneNumber: String, verificationCode: String)

    fun setUpAuthCallbacks(authenticationCallbacks: AuthenticationCallbacks)

    fun resendVerificationCode()
}
package com.codedevtech.mycardv2.services

import com.codedevtech.mycardv2.AuthenticationCallbacks
import com.google.firebase.auth.PhoneAuthCredential

interface AuthenticationService {

    fun sendVerificationCode(phoneNumber: String, timeoutInMillis: Long)

    suspend fun attemptAuth(phoneNumber: String, verificationCode: String)

    suspend fun attemptAuth(phoneAuthCredential: PhoneAuthCredential)

    fun setUpAuthCallbacks(authenticationCallbacks: AuthenticationCallbacks)

    fun resendVerificationCode()
}
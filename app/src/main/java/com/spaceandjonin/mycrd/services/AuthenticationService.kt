package com.spaceandjonin.mycrd.services


import com.spaceandjonin.mycrd.AuthenticationCallbacks
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential

interface AuthenticationService {

    fun sendVerificationCode(phoneNumber: String, timeoutInMillis: Long)

    suspend fun attemptAuth(phoneNumber: String, verificationCode: String)

    suspend fun attemptAuth(phoneAuthCredential: PhoneAuthCredential)

    fun setUpAuthCallbacks(authenticationCallbacks: AuthenticationCallbacks<FirebaseUser>)

    fun resendVerificationCode()
}
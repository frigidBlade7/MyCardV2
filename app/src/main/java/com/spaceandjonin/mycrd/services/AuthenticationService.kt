package com.spaceandjonin.mycrd.services


import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.spaceandjonin.mycrd.listeners.AuthenticationCallbacks

interface AuthenticationService {

    fun sendVerificationCode(phoneNumber: String, timeoutInMillis: Long)

    suspend fun attemptAuth(phoneNumber: String, verificationCode: String)

    suspend fun attemptAuth(phoneAuthCredential: PhoneAuthCredential)

    fun setUpAuthCallbacks(authenticationCallbacks: AuthenticationCallbacks<FirebaseUser>)

    fun resendVerificationCode()

    //todo get rid of this hacky solution
    fun setActivity(activity: Activity)
}
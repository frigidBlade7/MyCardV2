package com.codedevtech.mycardv2.services

import android.util.Log
import com.codedevtech.mycardv2.AuthenticationCallbacks
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.models.User
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit
import com.codedevtech.mycardv2.utils.getCode
import com.google.firebase.firestore.FirebaseFirestore
import com.codedevtech.mycardv2.R

import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
 class AuthenticationServiceImpl @Inject constructor(private val auth: FirebaseAuth): AuthenticationService{

    //private val phoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
    private val phoneAuthProvider = PhoneAuthProvider.getInstance(auth)
    private lateinit var verificationId: String
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var phoneNumber: String
    private var timeoutInMillis: Long = 0

    private lateinit var authCallback: AuthenticationCallbacks<FirebaseUser>

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            authCallback.onAuthCredentialSent(p0)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            authCallback.onAuthFailure(p0.getCode())
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            super.onCodeAutoRetrievalTimeOut(p0)
            authCallback.onCodeTimeout()
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            forceResendingToken =p1
            verificationId = p0

            authCallback.onCodeSent()
            super.onCodeSent(p0, p1)
        }



    }
    override fun sendVerificationCode(phoneNumber: String, timeoutInMillis: Long) {
       /* todo migrate to this when dependency on activity is removed
           phoneAuthOptions
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeoutInMillis,TimeUnit.MILLISECONDS)
            .setCallbacks(callbacks)*/
        this.timeoutInMillis = timeoutInMillis
        this.phoneNumber = phoneNumber

        phoneAuthProvider.verifyPhoneNumber(phoneNumber,timeoutInMillis,TimeUnit.MILLISECONDS,TaskExecutors.MAIN_THREAD,
            callbacks)
    }

    override suspend fun attemptAuth(phoneNumber: String, verificationCode: String) {
        try{
            val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,verificationCode)
            val data = auth.signInWithCredential(phoneAuthCredential).await()
            data.user?.let {
                authCallback.onAuthSuccess(it)
            }
        }catch (e: Exception){
            Log.d(TAG, "attemptAuth: ${e.localizedMessage}")
            authCallback.onAuthFailure(e.getCode())
        }
    }

    override suspend fun attemptAuth(phoneAuthCredential: PhoneAuthCredential) {
        try{
            val data = auth.signInWithCredential(phoneAuthCredential).await()
            data.user?.let {
                authCallback.onAuthSuccess(it)
            }
        }catch (e: Exception){
            Log.d(TAG, "attemptAuth: ${e.localizedMessage}")
            authCallback.onAuthFailure(e.getCode())
        }
    }


    override fun setUpAuthCallbacks(authenticationCallbacks: AuthenticationCallbacks<FirebaseUser>) {
        this.authCallback = authenticationCallbacks
    }

    override fun resendVerificationCode(){
        phoneAuthProvider.verifyPhoneNumber(phoneNumber,timeoutInMillis,TimeUnit.MILLISECONDS,TaskExecutors.MAIN_THREAD,
            callbacks, forceResendingToken)

    }

    companion object {
        private const val TAG = "AuthenticationServiceIm"
    }


}
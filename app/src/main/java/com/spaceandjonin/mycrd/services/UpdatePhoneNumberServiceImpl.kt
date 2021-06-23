package com.spaceandjonin.mycrd.services

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.spaceandjonin.mycrd.listeners.AuthenticationCallbacks
import com.spaceandjonin.mycrd.utils.getCode
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdatePhoneNumberServiceImpl @Inject constructor(private val auth: FirebaseAuth) :
    AuthenticationService {

    private val phoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
    private lateinit var verificationId: String
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var phoneNumber: String
    private var timeoutInMillis: Long = 0

    private lateinit var authCallback: AuthenticationCallbacks<FirebaseUser>

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
            super.onCodeSent(p0, p1)
            forceResendingToken = p1
            verificationId = p0

            authCallback.onCodeSent()
        }


    }

    override fun sendVerificationCode(phoneNumber: String, timeoutInMillis: Long) {

        this.timeoutInMillis = timeoutInMillis
        this.phoneNumber = phoneNumber


        setupPhoneAuthOptions()
        sendSms()
    }

    private fun sendSms() {
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions.build())
    }

    private fun setupPhoneAuthOptions() {
        phoneAuthOptions.setCallbacks(callbacks)
            .setPhoneNumber(phoneNumber)
            //todo .setActivity(TaskExecutors.MAIN_THREAD as Activity)
            .setTimeout(timeoutInMillis, TimeUnit.MILLISECONDS)

        forceResendingToken?.let {
            phoneAuthOptions.setForceResendingToken(it)
        }
    }


    override fun setActivity(activity: Activity) {
        phoneAuthOptions.setActivity(activity)
    }

    override suspend fun attemptAuth(phoneNumber: String, verificationCode: String) {

        //attempt auth now provides implementation for update number
        try {
            val phoneAuthCredential =
                PhoneAuthProvider.getCredential(verificationId, verificationCode)
            val data = auth.currentUser?.updatePhoneNumber(phoneAuthCredential)?.await()
            authCallback.onAuthSuccess(auth.currentUser!!)
        } catch (e: Exception) {
            Timber.d( "attemptAuth: ${e.localizedMessage}")
            authCallback.onAuthFailure(e.getCode())
        }
    }

    override suspend fun attemptAuth(phoneAuthCredential: PhoneAuthCredential) {
        //attempt auth now provides implementation for update number

        try {
            val data = auth.currentUser?.updatePhoneNumber(phoneAuthCredential)?.await()
            authCallback.onAuthSuccess(auth.currentUser!!)
        } catch (e: Exception) {
            Timber.d( "attemptAuth: ${e.localizedMessage}")
            authCallback.onAuthFailure(e.getCode())
        }
    }


    override fun setUpAuthCallbacks(authenticationCallbacks: AuthenticationCallbacks<FirebaseUser>) {
        this.authCallback = authenticationCallbacks
    }

    override fun resendVerificationCode() {
        setupPhoneAuthOptions()
        sendSms()
    }

    companion object {
        private const val TAG = "AuthenticationServiceIm"
    }


}
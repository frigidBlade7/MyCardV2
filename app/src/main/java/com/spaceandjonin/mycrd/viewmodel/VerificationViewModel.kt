package com.spaceandjonin.mycrd.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.spaceandjonin.mycrd.di.UpdateService
import com.spaceandjonin.mycrd.event.Event
import com.spaceandjonin.mycrd.fragments.settings.VerifyNewNumberFragmentDirections
import com.spaceandjonin.mycrd.listeners.AuthenticationCallbacks
import com.spaceandjonin.mycrd.services.AuthenticationService
import com.spaceandjonin.mycrd.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    @UpdateService val updateNumberService: AuthenticationService,
) : BaseViewModel() {

    val isVerifyButtonEnabled = MutableLiveData<Boolean>(true)
    val isResendButtonEnabled = MutableLiveData<Boolean>(true)
    val smsCode = MutableLiveData<String>()
    val newPhoneNumber = MutableLiveData<String>()

    private val authCallbacks = object : AuthenticationCallbacks<FirebaseUser>() {
        override fun onCodeSent() {
            //start countdown
            isVerifyButtonEnabled.value = true
            isResendButtonEnabled.value = false
        }

        override fun onAuthSuccess(userObject: FirebaseUser) {
            //go back to settings, we successfully updated the user's phone number
            _destination.value =
                Event(VerifyNewNumberFragmentDirections.actionVerifyNewNumberFragmentToSettingsFragment())

        }

        override fun onAuthCredentialSent(phoneAuthCredential: PhoneAuthCredential) {
            viewModelScope.launch {
                updateNumberService.attemptAuth(phoneAuthCredential)
            }
        }

        override fun onAuthFailure(errorCode: Int) {
            _snackbarInt.postValue(Event(errorCode))
        }

        override fun onCodeTimeout() {
            //countdown complete
            isResendButtonEnabled.value = true

        }

    }

    fun resendCode() {
        updateNumberService.resendVerificationCode()
    }

    fun attemptAuth(phoneNumber: String) {
        viewModelScope.launch {
            smsCode.value?.let {
                updateNumberService.attemptAuth(phoneNumber, it)
            }
        }

    }


    fun sendVerificationCode(phoneNumber: String) {
        updateNumberService.setUpAuthCallbacks(authCallbacks)
        updateNumberService.sendVerificationCode(phoneNumber, Utils.TIMEOUT)
    }


}
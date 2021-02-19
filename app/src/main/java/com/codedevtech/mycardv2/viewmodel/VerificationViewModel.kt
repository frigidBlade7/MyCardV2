package com.codedevtech.mycardv2.viewmodel

import androidx.lifecycle.*
import com.codedevtech.mycardv2.AuthenticationCallbacks
import com.codedevtech.mycardv2.di.UpdateService
import com.codedevtech.mycardv2.services.AuthenticationService
import com.codedevtech.mycardv2.event.Event
import com.codedevtech.mycardv2.fragments.onboarding.*
import com.codedevtech.mycardv2.utils.Utils
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    @UpdateService private val updateNumberService: AuthenticationService,
) : BaseViewModel() {

    var isVerifyButtonEnabled = MutableLiveData<Boolean>(true)
    var isResendButtonEnabled = MutableLiveData<Boolean>(true)
    var smsCode = MutableLiveData<String>()
    var newPhoneNumber = MutableLiveData<String>()

    var authCallbacks = object : AuthenticationCallbacks<FirebaseUser>(){
        override fun onCodeSent() {
            //start countdown
            isVerifyButtonEnabled.value = true
            isResendButtonEnabled.value = false
        }

        override fun onAuthSuccess(userObject: FirebaseUser) {
            //go back to settings, we successfully updated the user's phone number
            _destination.value = Event(VerifyNewNumberFragmentDirections.actionVerifyNewNumberFragmentToSettingsFragment())

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

    fun resendCode(){
        updateNumberService.resendVerificationCode()
    }

    fun attemptAuth(phoneNumber: String){
        viewModelScope.launch {
            smsCode.value?.let {
                updateNumberService.attemptAuth(phoneNumber, it)
            }
        }

    }




    fun sendVerificationCode(phoneNumber: String){
        updateNumberService.setUpAuthCallbacks(authCallbacks)
        updateNumberService.sendVerificationCode(phoneNumber, Utils.TIMEOUT)
    }


}
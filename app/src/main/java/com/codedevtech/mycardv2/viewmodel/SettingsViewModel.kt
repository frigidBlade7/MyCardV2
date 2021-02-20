package com.codedevtech.mycardv2.viewmodel

import android.net.Uri
import android.telephony.PhoneNumberUtils
import androidx.lifecycle.*
import androidx.navigation.ActionOnlyNavDirections
import com.codedevtech.mycardv2.AuthenticationCallbacks
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.SettingsNavDirections
import com.codedevtech.mycardv2.db.AppDb
import com.codedevtech.mycardv2.di.AuthService
import com.codedevtech.mycardv2.services.AuthenticationService
import com.codedevtech.mycardv2.event.Event
import com.codedevtech.mycardv2.fragments.dashboard.*
import com.codedevtech.mycardv2.fragments.onboarding.*
import com.codedevtech.mycardv2.models.*
import com.codedevtech.mycardv2.models.datasource.AddedCardDataSource
import com.codedevtech.mycardv2.models.datasource.FirebaseAddedCardDataSourceImpl
import com.codedevtech.mycardv2.models.datasource.FirebaseUserDataSourceImpl
import com.codedevtech.mycardv2.services.UpdateImageService
import com.codedevtech.mycardv2.utils.Utils
import com.codedevtech.mycardv2.utils.notifyObserver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val uploadService: UpdateImageService,
    private val auth: FirebaseAuth,
    @AuthService private val authenticationService: AuthenticationService,
    private val userDataSourceImpl: FirebaseUserDataSourceImpl, val appDb: AppDb, val db: FirebaseFirestore
) : BaseViewModel() {

    var profileImageUri = MutableLiveData<Uri>()
    var user = MutableLiveData<User>(User(auth.uid!!))
    var isVerifyButtonEnabled = MutableLiveData<Boolean>(true)
    var isResendButtonEnabled = MutableLiveData<Boolean>(true)
    var smsCode = MutableLiveData<String>()

    fun getUser(): LiveData<Resource<User>> {
        return userDataSourceImpl.getData(""/*for future use with multi logins*/).asLiveData()
    }

    var authCallbacks = object : AuthenticationCallbacks<FirebaseUser>(){
        override fun onCodeSent() {
            //start countdown
            isVerifyButtonEnabled.value = true
            isResendButtonEnabled.value = false
        }

        override fun onAuthSuccess(userObject: FirebaseUser) {
            //go to update number
            _destination.value = Event(VerifyCurrentNumberFragmentDirections.actionVerifyCurrentNumberFragmentToUpdateNumberFragment())

        }

        override fun onAuthCredentialSent(phoneAuthCredential: PhoneAuthCredential) {
            viewModelScope.launch {
                authenticationService.attemptAuth(phoneAuthCredential)
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
        authenticationService.resendVerificationCode()
    }

    fun attemptAuth(phoneNumber: String){
        viewModelScope.launch {
            smsCode.value?.let {
                authenticationService.attemptAuth(phoneNumber, it)
            }
        }

    }
    fun goToUpdateNumber(){
        _destination.value = Event(SettingsFragmentDirections.actionSettingsFragmentToConfirmNumberResetFragment())
    }
    fun goToUpdateProfile() {
        _destination.value = Event(SettingsFragmentDirections.actionSettingsFragmentToUpdateDisplayNameFragment())
    }

    fun updateProfile() {
        profileImageUri.value?.let {
            viewModelScope.launch {
                uploadService.setUri(it)
                when(val uploadData = uploadService.uploadImage("profiles/${auth.currentUser?.uid}")){
                    is Resource.Success->{
                        when (val profileData = userDataSourceImpl.updateImage(uploadData.data)){
                            is Resource.Error ->{
                                _snackbarInt.postValue(Event(profileData.errorCode))
                            }
                            is Resource.Success->{
                                user.value?.profileUrl = profileData.data
                                user.notifyObserver()
                            }
                        }

                    }
                    is Resource.Error->{
                        _snackbarInt.postValue(Event(uploadData.errorCode))

                    }
                }
            }

        }
    }

    fun logout(){
        auth.addAuthStateListener {
            if (it.currentUser == null) {
                _destination.value = Event(SettingsNavDirections.actionGlobalWelcomeFragment())
                viewModelScope.launch(Dispatchers.IO) {
                    appDb.clearAllTables()
                    db.clearPersistence()
                }
            }

        }
        auth.signOut()
    }


    fun sendVerificationCode(phoneNumber: String){
        authenticationService.setUpAuthCallbacks(authCallbacks)
        authenticationService.sendVerificationCode(phoneNumber, Utils.TIMEOUT)
    }

    fun updateProfileNumber(){
        _destination.value = Event(ConfirmNumberResetFragmentDirections.actionConfirmNumberResetFragmentToVerifyCurrentNumberFragment())
    }

    fun updateProfileName(){
        viewModelScope.launch {
            when(val profileData = userDataSourceImpl.updateData(user.value!!)){
                is Resource.Error ->{
                    _snackbarInt.postValue(Event(profileData.errorCode))
                }
                is Resource.Success->{
                    user.value?.profileUrl = profileData.data
                    user.notifyObserver()
                    _destination.postValue(Event(ActionOnlyNavDirections(0)))
                }
            }
        }
    }

}
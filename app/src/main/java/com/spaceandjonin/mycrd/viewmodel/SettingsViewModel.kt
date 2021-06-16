package com.spaceandjonin.mycrd.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.ActionOnlyNavDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.SettingsNavDirections
import com.spaceandjonin.mycrd.db.AppDb
import com.spaceandjonin.mycrd.di.AuthService
import com.spaceandjonin.mycrd.di.ImageFile
import com.spaceandjonin.mycrd.event.Event
import com.spaceandjonin.mycrd.fragments.settings.ConfirmNumberResetFragmentDirections
import com.spaceandjonin.mycrd.fragments.settings.SettingsFragmentDirections
import com.spaceandjonin.mycrd.fragments.settings.VerifyCurrentNumberFragmentDirections
import com.spaceandjonin.mycrd.listeners.AuthenticationCallbacks
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.models.User
import com.spaceandjonin.mycrd.repositories.UserRepository
import com.spaceandjonin.mycrd.services.AuthenticationService
import com.spaceandjonin.mycrd.services.UpdateImageService
import com.spaceandjonin.mycrd.utils.Utils
import com.spaceandjonin.mycrd.utils.notifyObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val uploadService: UpdateImageService,
    private val auth: FirebaseAuth,
    @ImageFile val imageFile: File?,
    @AuthService val authenticationService: AuthenticationService,
    private val userRepository: UserRepository, val appDb: AppDb, val db: FirebaseFirestore
) : BaseViewModel() {

    var profileImageUri = MutableLiveData<Uri>(Uri.EMPTY)
    var user = MutableLiveData(User(auth.uid!!))
    var isVerifyButtonEnabled = MutableLiveData(true)
    var isResendButtonEnabled = MutableLiveData(true)
    var smsCode = MutableLiveData<String>()

    fun getLoggedInUser(): LiveData<Resource<User>> {
        return userRepository.getLoggedInUser()
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

    fun showPhotoOptions(){
        val action = SettingsFragmentDirections.actionSettingsFragmentToProfilePhotoActionsFragment()
        _destination.value = Event(action)
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
                        when (val profileData = userRepository.userDataSourceImpl.updateImage(uploadData.data)){
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
            when(val profileData = userRepository.userDataSourceImpl.updateData(user.value!!)){
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

    fun removePhoto() {
        profileImageUri.value = Uri.EMPTY

        viewModelScope.launch {

            profileImageUri.value?.let {
                when(val data = userRepository.userDataSourceImpl.updateImage(it)){
                    is Resource.Success -> {
                        //todo hide loader
                            try {
                                FirebaseStorage.getInstance().reference.child("images")
                                    .child("profiles/${auth.currentUser?.uid}").delete().await()


                                user.value?.profileUrl = ""
                                user.notifyObserver()
                                _snackbarInt.postValue(Event(R.string.success))
                            } catch (e: Exception) {
                                Log.d(Companion.TAG, "removePhoto: ${e.localizedMessage}")
                            }

                    }
                    is Resource.Error ->{
                        //todo hide loader
                        _snackbarInt.postValue(Event(data.errorCode))

                    }

                    is Resource.Loading->{
                        //todo show loader
                        _snackbarInt.postValue(Event(R.string.adding_card))
                    }
                }
            }
            //onsuccess
        }
    }

    companion object {
        private const val TAG = "SettingsViewModel"
    }

}
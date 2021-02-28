package com.codedevtech.mycardv2.viewmodel

import android.net.Uri
import android.telephony.PhoneNumberUtils
import androidx.lifecycle.*
import androidx.navigation.ActionOnlyNavDirections
import com.codedevtech.mycardv2.AuthenticationCallbacks
import com.codedevtech.mycardv2.R
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle,
                                              private val uploadService: UpdateImageService,
                                              private val auth: FirebaseAuth,
                                              @AuthService private val authenticationService: AuthenticationService,
                                              private val userDataSourceImpl: FirebaseUserDataSourceImpl
) : BaseViewModel() {

    lateinit var filePath: String
    var profileImageUri = MutableLiveData<Uri>()

    var selectedCard = MutableLiveData<AddedCard>()
    var selectedPersonalCard = MutableLiveData<LiveCard>()
    var position = MutableLiveData<Int>()

    //private val authenticationService: AuthenticationService = AuthenticationServiceImpl(auth)

    var phoneNumber = MutableLiveData<String>()
    var phoneNumberFormatted = MutableLiveData<String>()
    var name = MutableLiveData<String>("")

    var isVerifyButtonEnabled = MutableLiveData<Boolean>(true)
    var isResendButtonEnabled = MutableLiveData<Boolean>(true)
    var smsCode = MutableLiveData<String>()


    var authCallbacks = object : AuthenticationCallbacks<FirebaseUser>(){
        override fun onCodeSent() {
            //start countdown
            isVerifyButtonEnabled.value = true
            isResendButtonEnabled.value = false
        }

        override fun onAuthSuccess(userObject: FirebaseUser) {
            //go to dashboard if user is already created i.e. has display name

            userObject.displayName?.let {
                if(it.isEmpty()) {
                    goToCompleteSetup()
                    return
                }
                goToDashboard()
                return
            }
            goToCompleteSetup()
/*            viewModelScope.launch {
                val user = User(userObject.uid,phoneNumber = userObject.phoneNumber, name = name.value)

                when (val data = userDataSourceImpl.addData(user)){
                    is Resource.Success-> goToDashboard()
                    is Resource.Error-> _snackbarInt.postValue(Event(data.errorCode))
                }
            }*/

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

    fun goToSkip(){
        val action = //WelcomeFragmentDirections.actionWelcomeFragmentToSkipOnboardingFragment()
            WelcomeFragmentDirections.actionWelcomeFragmentToSignUpFragment()
        _destination.value = Event(action)
    }

    fun goToScan(){
        val action = SkipOnboardingFragmentDirections.actionSkipOnboardingFragmentToCaptureCardFragment()
        _destination.value = Event(action)

    }

    fun goToConfirm(){
        val action = CaptureCardFragmentDirections.actionCaptureCardFragmentToConfirmDetailsFragment()
        _destination.value = Event(action)
    }

    fun goToSetup(){

        val action = ActionOnlyNavDirections(R.id.action_confirmDetailsFragment_to_setUpAccountFragment)
        _destination.value = Event(action)
    }

    fun skipToSignUp(){
        val action = SkipOnboardingFragmentDirections.actionSkipOnboardingFragmentToSignUpFragment()
        _destination.value = Event(action)
    }

    fun goToAddCard(){
        _destination.value = Event(CardsFragmentDirections.actionCardsFragmentToAddCardNav())
    }

    fun getUser(): LiveData<Resource<User>> {
        return userDataSourceImpl.getData(""/*for future use with multi logins*/).asLiveData()

    }

/*    fun deleteCard(){
        selectedCard.value?.let {
            viewModelScope.launch {
                when(val deleteData = addedCardDataSourceImpl.removeData(it)){
                    is Resource.Success->{
                        _destination.value = Event(CardsFragmentDirections.actionGlobalCardsFragment())
                    }
                    is Resource.Error-> _snackbarInt.postValue(Event(deleteData.errorCode))
                }
            }

        }

    }*/
    fun goToAddCardFromCapture(){
        _destination.value = Event(CaptureCardFragmentDirections.actionCaptureCardFragmentToAddCardNav())
    }

    fun goToSignUp(){
        val action = SetUpAccountFragmentDirections.actionSetUpAccountFragmentToSignUpFragment()
        _destination.value = Event(action)
    }

    fun goToVerify(/*number: String*/){

/*
        if(!PhoneNumberUtils.isGlobalPhoneNumber(number.trim())) {
            _snackbarInt.postValue(Event(R.string.enter_valid_number))
            return
        }
*/

        val action = ConfirmNumberDialogFragmentDirections.actionConfirmNumberFragmentToVerifyNumberFragment()
        _destination.value = Event(action)

    }

    fun goToConfirmNumber(number: String){

        if(!PhoneNumberUtils.isGlobalPhoneNumber(number.trim())) {
            _snackbarInt.postValue(Event(R.string.enter_valid_number))
            return
        }

        val action = SignUpFragmentDirections.actionSignUpFragmentToConfirmNumberFragment()
        _destination.value = Event(action)

    }



    fun sendVerificationCode(phoneNumber: String){
        authenticationService.setUpAuthCallbacks(authCallbacks)
        authenticationService.sendVerificationCode(phoneNumber, Utils.TIMEOUT)
    }

    fun goToDashboard(){
        val action = VerifyNumberFragmentDirections.actionVerifyNumberFragmentToCardsFragment()
        _destination.value = Event(action)
    }
    fun goToDashboardAfterSetup(){
        val action = CompleteProfileFragmentDirections.actionSetupProfileFragmentToCardsFragment()
        _destination.value = Event(action)
    }

    fun goToCompleteSetup(){
        val action = VerifyNumberFragmentDirections.actionVerifyNumberFragmentToSetupProfileFragment()
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

    fun showCardOptions(isEdit: Boolean) {
        _destination.value = Event(CardOptionsFragmentDirections.actionGlobalCardOptionsFragment(isEdit))
    }

    fun showPersonalCardOptions() {
        _destination.value = Event(PersonalCardOptionsFragmentDirections.actionGlobalPersonalCardOptionsFragment())
    }

    fun confirmCardDeletion() {
        _destination.value = Event(DeleteCardDialogFragmentDirections.actionGlobalDeleteCardDialogFragment())
    }

    fun confirmPersonalCardDeletion() {
        _destination.value = Event(DeletePersonalCardDialogFragmentDirections.actionGlobalDeletePersonalCardDialogFragment())
    }

    fun showShare() {
        //TODO("Not yet implemented")
    }

    fun goToSettings() {
        _destination.value = Event(MeFragmentDirections.actionMeFragmentToSettingsNav())
    }


    fun goToAddPersonalCard() {
        _destination.value = Event(MeFragmentDirections.actionMeFragmentToAddPersonalCardNav())
    }

    fun showCardQr() {
        _destination.value = Event(ActionOnlyNavDirections(R.id.viewCardQrFragment))
    }


    fun showFilter(){
        _destination.value = Event(CardsFragmentDirections.actionCardsFragmentToCardFilterFragment())
    }

    fun goToSearch(){
        _destination.value = Event(CardsFragmentDirections.actionCardsFragmentToSearchCardsFragment())
    }

    fun completeProfile(){
        val user = User(auth.currentUser?.uid!!,auth.currentUser?.phoneNumber,name.value)
        viewModelScope.launch {
            when(val userData = userDataSourceImpl.addData(user)){
                is Resource.Success ->{
                    goToDashboardAfterSetup()
                }
                is Resource.Error ->{
                    _snackbarInt.postValue(Event(userData.errorCode))
                }
            }
        }
        updateProfile()
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
                        }

                    }
                    is Resource.Error->{
                        _snackbarInt.postValue(Event(uploadData.errorCode))

                    }
                }
            }

        }
    }

    fun editCard() {
        _destination.value = Event(CardOptionsFragmentDirections.actionCardOptionsFragmentToAddCardNav(isEdit = true, existingCard = selectedCard.value))
    }

    /*fun createFile(outputDirectory: File): File {
        return File(outputDirectory, SimpleDateFormat(Utils.IMAGE_FILE_FORMAT, Locale.ENGLISH)
            .format(System.currentTimeMillis()) + Utils.PHOTO_EXTENSION)
    }*/

/*    fun doIt(){
        Executors.newSingleThreadExecutor().execute {

        }
    }*/



}
package com.codedevtech.mycardv2.viewmodel

import android.telephony.PhoneNumberUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.ActionOnlyNavDirections
import com.codedevtech.mycardv2.AuthenticationCallbacks
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.services.AuthenticationService
import com.codedevtech.mycardv2.event.Event
import com.codedevtech.mycardv2.fragments.dashboard.CardsFragment
import com.codedevtech.mycardv2.fragments.dashboard.DashboardFragmentDirections
import com.codedevtech.mycardv2.fragments.onboarding.*
import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle,
                                              private val authenticationService: AuthenticationService ) : BaseViewModel() {

    lateinit var filePath: String

    var selectedCard = MutableLiveData<Card>()

    //private val authenticationService: AuthenticationService = AuthenticationServiceImpl(auth)

    var phoneNumber = MutableLiveData<String>()
    var isVerifyButtonEnabled = MutableLiveData<Boolean>(true)
    var isResendButtonEnabled = MutableLiveData<Boolean>(true)
    var smsCode = MutableLiveData<String>()


    var authCallbacks = object : AuthenticationCallbacks(){
        override fun onCodeSent() {
            //start countdown
            isVerifyButtonEnabled.value = true
            isResendButtonEnabled.value = false
        }

        override fun onAuthSuccess() {
            //go to dashboard
            goToDashboard()
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
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToSkipOnboardingFragment()
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
        _destination.value = Event(DashboardFragmentDirections.actionDashboardFragmentToAddCardNav())
    }

    fun goToAddCardFromCapture(){
        _destination.value = Event(CaptureCardFragmentDirections.actionCaptureCardFragmentToAddCardNav())
    }

    fun goToSignUp(){
        val action = SetUpAccountFragmentDirections.actionSetUpAccountFragmentToSignUpFragment()
        _destination.value = Event(action)
    }

    fun goToVerify(){

        val number = phoneNumber.value?.trim()

        if(!PhoneNumberUtils.isGlobalPhoneNumber(number)) {
            _snackbarInt.postValue(Event(R.string.enter_valid_number))
            return
        }

        val action = SignUpFragmentDirections.actionSignUpFragmentToVerifyNumberFragment()
        _destination.value = Event(action)

    }

    fun resendCode(){
        authenticationService.resendVerificationCode()
    }

    fun sendVerificationCode(phoneNumber: String){
        authenticationService.setUpAuthCallbacks(authCallbacks)
        authenticationService.sendVerificationCode(phoneNumber, Utils.TIMEOUT)
    }

    fun goToDashboard(){

        val action = VerifyNumberFragmentDirections.actionVerifyNumberFragmentToDashboardFragment()
        _destination.value = Event(action)
    }

    fun goToCardDetails(card: Card?){
        val action = DashboardFragmentDirections.actionDashboardFragmentToCardDetailsFragment(card)
        _destination.postValue(Event(action))
    }

    fun attemptAuth(phoneNumber: String){
        viewModelScope.launch {
            smsCode.value?.let {
                authenticationService.attemptAuth(phoneNumber, it)
            }
        }

    }
    fun showCardOptions() {
        _destination.value = Event(CardDetailsFragmentDirections.actionCardDetailsFragmentToCardOptionsFragment())
    }

    fun confirmCardDeletion() {
        _destination.value = Event(ActionOnlyNavDirections(R.id.deleteCardDialogFragment))
    }

    fun showShare() {
        //TODO("Not yet implemented")
    }

    fun showCardQr() {
        _destination.value = Event(ActionOnlyNavDirections(R.id.viewCardQrFragment))
    }


    fun showFilter(){
        _destination.value = Event(DashboardFragmentDirections.actionDashboardFragmentToCardFilterFragment())
    }

    fun goToSearch(){
        _destination.value = Event(DashboardFragmentDirections.actionDashboardFragmentToSearchCardsFragment())
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
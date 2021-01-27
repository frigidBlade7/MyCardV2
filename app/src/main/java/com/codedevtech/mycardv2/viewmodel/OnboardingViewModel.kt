package com.codedevtech.mycardv2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.navigation.ActionOnlyNavDirections
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.event.Event
import com.codedevtech.mycardv2.fragments.*
import com.codedevtech.mycardv2.fragments.dashboard.DashboardFragmentDirections
import com.codedevtech.mycardv2.fragments.dashboard.DeleteCardDialogFragment
import com.codedevtech.mycardv2.fragments.onboarding.*
import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.utils.Utils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class OnboardingViewModel : BaseViewModel() {

    lateinit var filePath: String

    var selectedCard = MutableLiveData<Card>()

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

    fun goToSignUp(){
        val action = SetUpAccountFragmentDirections.actionSetUpAccountFragmentToSignUpFragment()
        _destination.value = Event(action)
    }

    fun goToVerify(){
        val action = SignUpFragmentDirections.actionSignUpFragmentToVerifyNumberFragment()
        _destination.value = Event(action)
    }

    fun goToDashboard(){

        val action = VerifyNumberFragmentDirections.actionVerifyNumberFragmentToDashboardFragment()
        _destination.value = Event(action)
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
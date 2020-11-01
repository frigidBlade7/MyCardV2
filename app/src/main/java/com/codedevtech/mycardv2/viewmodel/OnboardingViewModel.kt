package com.codedevtech.mycardv2.viewmodel

import androidx.lifecycle.ViewModel
import com.codedevtech.mycardv2.event.Event
import com.codedevtech.mycardv2.fragments.*

class OnboardingViewModel : BaseViewModel() {

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

        val action = ConfirmDetailsFragmentDirections.actionConfirmDetailsFragmentToSetUpAccountFragment()
        _destination.value = Event(action)
    }
}
package com.codedevtech.mycardv2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codedevtech.mycardv2.AddCardNavDirections
import com.codedevtech.mycardv2.event.Event
import com.codedevtech.mycardv2.fragments.dashboard.AddPersonalCardFragmentDirections
import com.codedevtech.mycardv2.fragments.dashboard.AddWorkFragmentDirections
import com.codedevtech.mycardv2.fragments.dashboard.DashboardFragmentDirections
import com.codedevtech.mycardv2.fragments.onboarding.ConfirmAddDetailsFragment
import com.codedevtech.mycardv2.models.SocialMediaProfile
import com.codedevtech.mycardv2.viewmodel.BaseViewModel

class AddPersonalCardViewModel : BaseViewModel() {
    // TODO: Implement the ViewModel
    var isNameExpanded =  MutableLiveData<Boolean>(false)

    var socials = MutableLiveData(mutableListOf<SocialMediaProfile>(SocialMediaProfile(socialMedia= SocialMediaProfile.SocialMedia.LinkedIn),
        SocialMediaProfile(socialMedia= SocialMediaProfile.SocialMedia.Facebook),SocialMediaProfile(socialMedia= SocialMediaProfile.SocialMedia.Twitter),
        SocialMediaProfile(socialMedia= SocialMediaProfile.SocialMedia.Instagram)))

    fun goToSocialProfile(){
        _destination.value = Event(AddPersonalCardFragmentDirections.actionAddPersonalCardFragmentToAddSocialsFragment())
    }

    fun goToWorkProfile(){
        _destination.value = Event(AddPersonalCardFragmentDirections.actionAddPersonalCardFragmentToAddWorkFragment())
    }

    fun goToConfirmDetails(){
        _destination.value = Event(AddWorkFragmentDirections.actionAddWorkFragmentToConfirmAddDetailsFragment())
    }

    fun goToCard(){
        _destination.value = Event(AddCardNavDirections.actionGlobalCardDetailsFragment())
    }


}
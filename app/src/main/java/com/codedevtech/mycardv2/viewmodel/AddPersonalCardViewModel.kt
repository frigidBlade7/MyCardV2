package com.codedevtech.mycardv2.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codedevtech.mycardv2.AddCardNavDirections
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.event.Event
import com.codedevtech.mycardv2.fragments.dashboard.AddPersonalCardFragmentDirections
import com.codedevtech.mycardv2.fragments.dashboard.AddWorkFragmentDirections
import com.codedevtech.mycardv2.models.*
import com.codedevtech.mycardv2.repositories.CardsRepository
import com.codedevtech.mycardv2.services.UpdateImageService
import com.codedevtech.mycardv2.utils.aggregateNameToFullName
import com.codedevtech.mycardv2.utils.notifyObserver
import com.codedevtech.mycardv2.utils.segregateFullName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPersonalCardViewModel @Inject constructor(val cardsRepository: CardsRepository, val uploadService: UpdateImageService): BaseViewModel() {
    // TODO: Implement the ViewModel
    var isNameExpanded =  MutableLiveData<Boolean>(false)
    var profileImageUri = MutableLiveData<Uri>()
    var businessImageUri = MutableLiveData<Uri>()


    var card = MutableLiveData(Card())

    var name = MutableLiveData(Name())
    var socials = MutableLiveData(mutableListOf<SocialMediaProfile>(SocialMediaProfile(type= SocialMediaProfile.SocialMedia.LinkedIn),
        SocialMediaProfile(type= SocialMediaProfile.SocialMedia.Facebook),SocialMediaProfile(type= SocialMediaProfile.SocialMedia.Twitter),
        SocialMediaProfile(type= SocialMediaProfile.SocialMedia.Instagram)))

    var phoneNumbers = MutableLiveData(mutableListOf(PhoneNumber()))
    var emailAddresses = MutableLiveData(mutableListOf(EmailAddress()))

    var businessInfo = MutableLiveData(BusinessInfo())

    fun goToSocialProfile(){
        _destination.postValue(Event(AddPersonalCardFragmentDirections.actionAddPersonalCardFragmentToAddSocialsFragment()))
    }

    fun goToWorkProfile(){
        card.value?.name = name.value!!
        card.value?.socialMediaProfiles = socials.value?.filter { it.usernameOrUrl.isNotEmpty() }!!

        card.value?.emailAddresses = emailAddresses.value?.filter { it.address.isNotEmpty() }!!
        card.value?.phoneNumbers = phoneNumbers.value?.filter { it.number.isNotEmpty() }!!
        if(isNameExpanded.value!!)
            card.value?.name?.aggregateNameToFullName()
        else
            card.value?.name?.segregateFullName()

        card.notifyObserver()

        _destination.value = Event(AddPersonalCardFragmentDirections.actionAddPersonalCardFragmentToAddWorkFragment())
    }

    fun goToConfirmDetails(){
        card.value?.businessInfo = businessInfo.value!!
        card.notifyObserver()

        _destination.value = Event(AddWorkFragmentDirections.actionAddWorkFragmentToConfirmAddDetailsFragment())
    }

    fun goToCard(){
        //addCard()
        _destination.value = Event(AddCardNavDirections.actionGlobalCardDetailsFragment())
    }

    fun updateProfile(uri: Uri?) {
        profileImageUri.value = uri
        card.value?.profilePicUrl = uri.toString()
        card.notifyObserver()
    }

    fun updateBusiness(uri: Uri?) {
        businessImageUri.value = uri
        card.value?.businessInfo?.companyLogo = uri.toString()
        card.notifyObserver()
    }


    fun addCard(){

        //todo make this an observable return type
       viewModelScope.launch {
           card.value?.let {
               when(val data = cardsRepository.firebaseCardDataSource.addData(it)){
                   is Resource.Success->{
                       //todo hide loader
                       _snackbarInt.postValue(Event(R.string.success))
                       _destination.postValue(Event(AddCardNavDirections.actionGlobalCardDetailsFragment(card.value)))

                       updateBusinessLogo(data.data)
                       updateProfileImage(data.data)
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
       }

    }

    private fun updateProfileImage(cardId: String) {
        profileImageUri.value?.let { uri->

            viewModelScope.launch {
                uploadService.setUri(uri)
                when(val uploadData = uploadService.uploadImage("profiles/$cardId")){
                    is Resource.Success->{
                        //todo success upload _snackbarInt.postValue(Event(uploadData.errorCode))
                            when (val profileData = cardsRepository.firebaseCardDataSource.updateCardProfilePhoto(uploadData.data.toString(),cardId)){
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

    private fun updateBusinessLogo(cardId: String) {
        businessImageUri.value?.let { uri->

            viewModelScope.launch {
                uploadService.setUri(uri)
                when(val uploadData = uploadService.uploadImage("businesses/$cardId")){
                    is Resource.Success->{
                        //todo success upload _snackbarInt.postValue(Event(uploadData.errorCode))
                        when (val logoData = cardsRepository.firebaseCardDataSource.updateCardCompanyLogo(uploadData.data.toString(),cardId)){
                            is Resource.Error ->{
                                _snackbarInt.postValue(Event(logoData.errorCode))
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
}
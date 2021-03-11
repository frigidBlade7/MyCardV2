package com.codedevtech.mycardv2.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.ActionOnlyNavDirections
import androidx.work.*
import com.codedevtech.mycardv2.AddCardNavDirections
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.event.Event
import com.codedevtech.mycardv2.fragments.dashboard.AddCardFragmentDirections
import com.codedevtech.mycardv2.fragments.dashboard.AddWorkFragmentDirections
import com.codedevtech.mycardv2.models.*
import com.codedevtech.mycardv2.repositories.AddedCardsRepository
import com.codedevtech.mycardv2.services.UpdateImageService
import com.codedevtech.mycardv2.utils.Utils
import com.codedevtech.mycardv2.utils.aggregateNameToFullName
import com.codedevtech.mycardv2.utils.notifyObserver
import com.codedevtech.mycardv2.utils.segregateFullName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor(@ApplicationContext val applicationContext: Context, val addedCardsRepository: AddedCardsRepository, val uploadService: UpdateImageService): BaseViewModel() {

    var isNameExpanded =  MutableLiveData<Boolean>(false)
    var profileImageUri = MutableLiveData<Uri>()

    var isEditFlow = MutableLiveData(false)

/*    val _uploadWork = MutableLiveData<Event<OneTimeWorkRequest>>()

    val uploadWork: LiveData<Event<OneTimeWorkRequest>>
        get() = _uploadWork*/


    var card = MutableLiveData(AddedCard())
    
    var name = MutableLiveData(Name())
    var socials = MutableLiveData(mutableListOf<SocialMediaProfile>(SocialMediaProfile(type= SocialMediaProfile.SocialMedia.LinkedIn),
        SocialMediaProfile(type= SocialMediaProfile.SocialMedia.Facebook),SocialMediaProfile(type= SocialMediaProfile.SocialMedia.Twitter),
        SocialMediaProfile(type= SocialMediaProfile.SocialMedia.Instagram)))

    var phoneNumbers = MutableLiveData(mutableListOf(PhoneNumber()))
    var emailAddresses = MutableLiveData(mutableListOf(EmailAddress()))

    var businessInfo = MutableLiveData(BusinessInfo())
    var note = MutableLiveData("")

    fun goToSocialProfile(){
        _destination.postValue(Event(AddCardFragmentDirections.actionAddCardFragmentToAddSocialsFragment()))
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

        _destination.value = Event(AddCardFragmentDirections.actionAddCardFragmentToAddWorkFragment())
    }

    fun goToConfirmDetails(){
        card.value?.businessInfo = businessInfo.value!!
        card.notifyObserver()

        _destination.value = Event(AddWorkFragmentDirections.actionAddWorkFragmentToConfirmAddDetailsFragment())
    }

    fun goToCard(){
        card.value?.note = note.value
        card.notifyObserver()
        addCard()
        //_destination.value = Event(AddCardNavDirections.actionGlobalCardDetailsFragment())
    }

    fun updateProfile(uri: Uri?) {
        profileImageUri.value = uri
        card.value?.profilePicUrl = uri.toString()
        card.notifyObserver()
    }

    fun updateBusiness(uri: Uri?) {
        card.value?.businessInfo?.companyLogo = uri.toString()
        card.notifyObserver()
    }


    fun addCard(){

       viewModelScope.launch {
           card.value?.let {
               when(val data = addedCardsRepository.firebaseAddedCardDataSource.addData(it)){
                   is Resource.Success->{
                       //todo hide loader

                       _snackbarInt.postValue(Event(R.string.success))
                       _destination.postValue(Event(AddCardNavDirections.actionGlobalCardsFragment()))


                       data.data?.let {
                           card.value?.id = it
                           updateProfileImage(it)
                       }
                       //updateBusinessLogo(data.data)

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

    fun updateNote(){
        card.notifyObserver()
        viewModelScope.launch {
            card.value?.let {
                when(val data = addedCardsRepository.firebaseAddedCardDataSource.updateData(it)){
                    is Resource.Success->{
                        //todo hide loader
                        _destination.postValue(Event(ActionOnlyNavDirections(0)))
                        _snackbarInt.postValue(Event(R.string.success))
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

    fun updateCard(){

        card.value?.businessInfo = businessInfo.value!!
        card.value?.name = name.value!!
        card.value?.socialMediaProfiles = socials.value?.filter { it.usernameOrUrl.isNotEmpty() }!!

        card.value?.emailAddresses = emailAddresses.value?.filter { it.address.isNotEmpty() }!!
        card.value?.phoneNumbers = phoneNumbers.value?.filter { it.number.isNotEmpty() }!!
        if(isNameExpanded.value!!)
            card.value?.name?.aggregateNameToFullName()
        else
            card.value?.name?.segregateFullName()


        card.notifyObserver()
        viewModelScope.launch {
            card.value?.let {
                when(val data = addedCardsRepository.firebaseAddedCardDataSource.updateData(it)){
                    is Resource.Success->{
                        //todo hide loader
                        data.data.let {
                            card.value?.id = it
                            updateProfileImage(it)
                        }
                        _snackbarInt.postValue(Event(R.string.success))
                        _destination.postValue(Event(AddCardNavDirections.actionGlobalCardDetailsFragment(card.value)))

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

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val myUploadWork = OneTimeWorkRequestBuilder<FirebaseFirestoreUploadWorker>()
                .setInputData(workDataOf(
                    Utils.PATH_ID to "profiles/$cardId",
                    Utils.PHOTO_URI to uri.toString()
                ))
                .setConstraints(constraints)
                .addTag(cardId)
                .build()


            WorkManager.getInstance(applicationContext).enqueue(myUploadWork)
            //_uploadWork.postValue(Event(myUploadWork))


            /*viewModelScope.launch {
                uploadService.setUri(uri)
                when(val uploadData = uploadService.uploadImage("profiles/$cardId")){
                    is Resource.Success->{
                            when (val profileData = addedCardsRepository.firebaseAddedCardDataSource.updateCardProfilePhoto(uploadData.data.toString(),cardId)){
                                is Resource.Error ->{
                                    _snackbarInt.postValue(Event(profileData.errorCode))
                                }
                            }

                    }
                    is Resource.Error -> {
                        Log.d(Companion.TAG, "updateProfileImage: ${uploadData.errorCode}")
                        _snackbarInt.postValue(Event(uploadData.errorCode))

                    }
                }
            }
*/



        }
    }

/*    private fun updateBusinessLogo(cardId: String) {
        businessImageUri.value?.let { uri->

            viewModelScope.launch {
                uploadService.setUri(uri)
                when(val uploadData = uploadService.uploadImage("businesses/$cardId")){
                    is Resource.Success->{
                        //todo success upload _snackbarInt.postValue(Event(uploadData.errorCode))
                        when (val logoData = personalCardsRepository.firebaseCardDataSource.updateCardCompanyLogo(uploadData.data.toString(),cardId)){
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
    }*/
    companion object {
        private const val TAG = "AddCardViewModel"
    }
}
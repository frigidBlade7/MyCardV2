package com.spaceandjonin.mycrd.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.ActionOnlyNavDirections
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.spaceandjonin.mycrd.AddCardNavDirections
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.di.ImageFile
import com.spaceandjonin.mycrd.event.Event
import com.spaceandjonin.mycrd.fragments.dashboard.AddCardFragmentDirections
import com.spaceandjonin.mycrd.fragments.dashboard.AddWorkFragmentDirections
import com.spaceandjonin.mycrd.models.*
import com.spaceandjonin.mycrd.models.datasource.FirebaseAddedCardDataSourceImpl
import com.spaceandjonin.mycrd.repositories.AddedCardsRepository
import com.spaceandjonin.mycrd.utils.Utils
import com.spaceandjonin.mycrd.utils.aggregateNameToFullName
import com.spaceandjonin.mycrd.utils.notifyObserver
import com.spaceandjonin.mycrd.utils.segregateFullName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor( val addedCardsRepository: AddedCardsRepository,
                                            @ImageFile val imageFile: File?,
                                            val workManager: WorkManager): BaseViewModel() {

    var isNameExpanded =  MutableLiveData<Boolean>(false)
    var profileImageUri = MutableLiveData<Uri>(Uri.EMPTY)

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
        card.value?.socialMediaProfiles = socials.value?.filter { it.usernameOrUrl.trim().isNotEmpty() }!!

        card.value?.emailAddresses = emailAddresses.value?.filter { it.address.trim().isNotEmpty() }!!
        card.value?.phoneNumbers = phoneNumbers.value?.filter { it.number.trim().isNotEmpty() }!!
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
        uri?.let {
            profileImageUri.value = it
        }

        card.value?.profilePicUrl = uri.toString()

        card.notifyObserver()

        isEditFlow.value?.let {
            if(it)
                card.value?.let {
                    updateProfileImage(it.id)
                }
        }

    }

    fun updateBusiness(uri: Uri?) {
        card.value?.businessInfo?.companyLogo = uri.toString()
        card.notifyObserver()
    }

    fun removePhoto(){
        card.value?.profilePicUrl =""
        profileImageUri.value = Uri.EMPTY
        card.notifyObserver()

        viewModelScope.launch {

            card.value?.let {
                when(val data = addedCardsRepository.firebaseAddedCardDataSource.updateCardProfilePhoto("",it.id)){
                    is Resource.Success -> {
                        //todo hide loader

                        card.value?.let { card ->
                            try {
                                FirebaseStorage.getInstance().reference.child("images")
                                    .child("profiles/${card.id}").delete().await()

                                _snackbarInt.postValue(Event(R.string.success))
                            } catch (e: Exception) {
                                Log.d(TAG, "removePhoto: ${e.localizedMessage}")
                            }

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
//        Log.d(TAG, "updateProfileImage: ${profileImageUri.value.toString()}")

        //updateCard()
        //do it on the server

    }

    fun showPhotoOptions(){
        val action = AddCardFragmentDirections.actionAddCardFragmentToPhotoActionsFragment()
        _destination.value = Event(action)
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
        card.value?.note = note.value

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
        card.value?.socialMediaProfiles = socials.value?.filter { it.usernameOrUrl.trim().isNotEmpty() }!!

        card.value?.emailAddresses = emailAddresses.value?.filter { it.address.trim().isNotEmpty() }!!
        card.value?.phoneNumbers = phoneNumbers.value?.filter { it.number.trim().isNotEmpty() }!!
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
        profileImageUri.value?.let { uri ->


            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val myUploadWork = OneTimeWorkRequestBuilder<FirebaseFirestoreUploadWorker>()
                .setInputData(workDataOf(
                    Utils.PATH_ID to cardId,
                    Utils.PHOTO_URI to uri.toString()
                ))
                .setConstraints(constraints)
                .addTag(cardId)
                .build()


            workManager.enqueue(myUploadWork)
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
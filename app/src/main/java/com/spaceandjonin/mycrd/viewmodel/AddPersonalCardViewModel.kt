package com.spaceandjonin.mycrd.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.google.firebase.storage.FirebaseStorage
import com.spaceandjonin.mycrd.AddCardNavDirections
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.ScanNavDirections
import com.spaceandjonin.mycrd.di.ImageFile
import com.spaceandjonin.mycrd.event.Event
import com.spaceandjonin.mycrd.fragments.dashboard.AddPersonalCardFragmentDirections
import com.spaceandjonin.mycrd.fragments.dashboard.AddPersonalWorkFragmentDirections
import com.spaceandjonin.mycrd.models.*
import com.spaceandjonin.mycrd.repositories.PersonalCardsRepository
import com.spaceandjonin.mycrd.repositories.UserRepository
import com.spaceandjonin.mycrd.utils.Utils
import com.spaceandjonin.mycrd.utils.aggregateNameToFullName
import com.spaceandjonin.mycrd.utils.notifyObserver
import com.spaceandjonin.mycrd.utils.segregateFullName
import com.spaceandjonin.mycrd.workers.FirebaseFirestoreUploadWorkerLiveCards
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddPersonalCardViewModel @Inject constructor(
    val personalCardsRepository: PersonalCardsRepository,
    val workManager: WorkManager,
    val userRepository: UserRepository,
    @ImageFile val imageFile: File?
) : BaseViewModel() {

    val isNameExpanded = MutableLiveData<Boolean>(false)
    val profileImageUri = MutableLiveData<Uri>(Uri.EMPTY)
    val businessImageUri = MutableLiveData<Uri>()


    val isEditFlow = MutableLiveData(false)
    val card = MutableLiveData(LiveCard())

    val name = MutableLiveData(Name())
    val socials = MutableLiveData(
        mutableListOf<SocialMediaProfile>(
            SocialMediaProfile(type = SocialMediaProfile.SocialMedia.LinkedIn),
            SocialMediaProfile(type = SocialMediaProfile.SocialMedia.Facebook),
            SocialMediaProfile(type = SocialMediaProfile.SocialMedia.Twitter),
            SocialMediaProfile(type = SocialMediaProfile.SocialMedia.Instagram)
        )
    )

    val phoneNumbers = MutableLiveData(mutableListOf(PhoneNumber()))
    val emailAddresses = MutableLiveData(mutableListOf(EmailAddress()))

    val businessInfo = MutableLiveData(BusinessInfo())

    fun goToSocialProfile() {
        _destination.postValue(Event(AddPersonalCardFragmentDirections.actionAddPersonalCardFragmentToAddPersonalSocialsFragment()))
    }

    fun goToWorkProfile() {
        card.value?.name = name.value!!
        card.value?.socialMediaProfiles =
            socials.value?.filter { it.usernameOrUrl.trim().isNotEmpty() }!!

        card.value?.emailAddresses =
            emailAddresses.value?.filter { it.address.trim().isNotEmpty() }!!
        card.value?.phoneNumbers = phoneNumbers.value?.filter { it.number.trim().isNotEmpty() }!!
        if (isNameExpanded.value!!)
            card.value?.name?.aggregateNameToFullName()
        else
            card.value?.name?.segregateFullName()

        card.notifyObserver()

        _destination.value =
            Event(AddPersonalCardFragmentDirections.actionAddPersonalCardFragmentToAddPersonalWorkFragment())
    }

    fun goToConfirmDetails() {
        card.value?.businessInfo = businessInfo.value!!
        card.notifyObserver()

        _destination.value =
            Event(AddPersonalWorkFragmentDirections.actionAddPersonalWorkFragmentToConfirmAddPersonalDetailsFragment())
    }

    fun goToCard() {
        addCard()
        //_destination.value = Event(AddCardNavDirections.actionGlobalCardDetailsFragment())
    }

    fun removePhoto() {
        card.value?.profilePicUrl = ""
        profileImageUri.value = Uri.EMPTY
        card.notifyObserver()
        //do it on the server
        viewModelScope.launch {

            card.value?.let {
                when (val data =
                    personalCardsRepository.firebaseLiveCardDataSource.updateCardProfilePhoto(
                        "",
                        it.id
                    )) {
                    is Resource.Success -> {
                        //todo hide loader

                        card.value?.let { card ->
                            try {
                                FirebaseStorage.getInstance().reference.child("images")
                                    .child("profiles/${card.id}").delete().await()

                                _snackbarInt.postValue(Event(R.string.success))
                            } catch (e: Exception) {
                                Timber.d("removePhoto: ${e.localizedMessage}")
                            }

                        }

                    }
                    is Resource.Error -> {
                        //todo hide loader
                        _snackbarInt.postValue(Event(data.errorCode))

                    }

                    is Resource.Loading -> {
                        //todo show loader
                        _snackbarInt.postValue(Event(R.string.adding_card))
                    }
                }
            }
            //onsuccess
        }

        Timber.d( "updateProfileImage: ${profileImageUri.value.toString()}")

    }


    fun showPhotoOptions() {
        val action =
            AddPersonalCardFragmentDirections.actionAddPersonalCardFragmentToPersonalPhotoActionsFragment()
        _destination.value = Event(action)
    }


    fun updateProfile(uri: Uri?) {
        uri?.let {
            profileImageUri.value = it
        }
        card.value?.profilePicUrl = uri.toString()
        card.notifyObserver()

        isEditFlow.value?.let {
            if (it)
                card.value?.let {
                    updateProfileImage(it.id)
                }
        }
    }

    fun updateBusiness(uri: Uri?) {
        uri?.let {
            businessImageUri.value = it
        }
        card.value?.businessInfo?.companyLogo = uri.toString()
        card.notifyObserver()
    }

    fun updateCard() {
        card.value?.businessInfo = businessInfo.value!!
        card.value?.name = name.value!!
        card.value?.socialMediaProfiles =
            socials.value?.filter { it.usernameOrUrl.trim().isNotEmpty() }!!

        card.value?.emailAddresses =
            emailAddresses.value?.filter { it.address.trim().isNotEmpty() }!!
        card.value?.phoneNumbers = phoneNumbers.value?.filter { it.number.trim().isNotEmpty() }!!
        if (isNameExpanded.value!!)
            card.value?.name?.aggregateNameToFullName()
        else
            card.value?.name?.segregateFullName()


        card.notifyObserver()
        viewModelScope.launch {
            card.value?.let {
                when (val data =
                    personalCardsRepository.firebaseLiveCardDataSource.updateData(it)) {
                    is Resource.Success -> {
                        //todo hide loader


                        data.data.let {
                            card.value?.id = it
                            updateProfileImage(it)

                        }


                        _snackbarInt.postValue(Event(R.string.success))
                        _destination.postValue(Event(AddCardNavDirections.actionGlobalMeFragment()))

                        //_destination.postValue(Event(AddPersonalCardNavDirections.actionGlobalCardPersonalDetailsFragment(card.value)))

                    }
                    is Resource.Error -> {
                        //todo hide loader
                        _snackbarInt.postValue(Event(data.errorCode))

                    }

                    is Resource.Loading -> {
                        //todo show loader
                        _snackbarInt.postValue(Event(R.string.adding_card))
                    }
                }
            }
        }
    }

    fun addCard() {

        viewModelScope.launch {
            card.value?.let {
                when (val data = personalCardsRepository.firebaseLiveCardDataSource.addData(it)) {
                    is Resource.Success -> {
                        //todo hide loader
                        //card.value?.id = data.data!!

                        _snackbarInt.postValue(Event(R.string.success))
                        userRepository.userDataSourceImpl.getData(null)
                            .catch {
                                emit(Resource.Error(R.id.error))
                            }.collect { userResource ->
                                when (userResource) {
                                    is Resource.Success -> {
                                        if (userResource.data.name.isNullOrEmpty()) {
                                            _destination.postValue(Event(ScanNavDirections.actionGlobalSetupProfileFragment()))
                                        } else {
                                            _destination.postValue(Event(ScanNavDirections.actionGlobalMeFragment()))
                                        }
                                    }
                                    is Resource.Loading -> {
                                        //todo show loader

                                    }
                                    is Resource.Error -> {
                                        _snackbarInt.postValue(Event(userResource.errorCode))
                                    }
                                }
                            }

                        data.data?.let {
                            card.value?.id = it
                            updateProfileImage(it)
                        }
                    }
                    is Resource.Error -> {
                        //todo hide loader
                        _snackbarInt.postValue(Event(data.errorCode))

                    }

                    is Resource.Loading -> {
                        //todo show loader
                        _snackbarInt.postValue(Event(R.string.adding_card))
                    }
                }
            }
        }

    }

    private fun updateProfileImage(cardId: String) {
        Timber.d( "updateProfileImage: ")
        profileImageUri.value?.let { uri ->

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val myUploadWork = OneTimeWorkRequestBuilder<FirebaseFirestoreUploadWorkerLiveCards>()
                .setInputData(
                    workDataOf(
                        Utils.PATH_ID to cardId,
                        Utils.PHOTO_URI to uri.toString()
                    )
                )
                .setConstraints(constraints)
                .addTag(cardId)
                .build()

            workManager.enqueue(myUploadWork)
        }
    }
}
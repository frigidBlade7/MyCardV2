package com.spaceandjonin.mycrd.viewmodel

import android.util.Patterns
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.ScanNavDirections
import com.spaceandjonin.mycrd.event.Event
import com.spaceandjonin.mycrd.fragments.scan.AddLabelledDetailFragmentDirections
import com.spaceandjonin.mycrd.fragments.scan.EditDetailFragmentDirections
import com.spaceandjonin.mycrd.fragments.scan.ReviewScannedDetailsFragmentDirections
import com.spaceandjonin.mycrd.fragments.scan.TakeCardPictureFragmentDirections
import com.spaceandjonin.mycrd.models.*
import com.spaceandjonin.mycrd.repositories.AddedCardsRepository
import com.spaceandjonin.mycrd.repositories.PersonalCardsRepository
import com.spaceandjonin.mycrd.repositories.UserRepository
import com.spaceandjonin.mycrd.services.SessionManagerService
import com.spaceandjonin.mycrd.utils.Utils
import com.spaceandjonin.mycrd.utils.notifyObserver
import com.spaceandjonin.mycrd.utils.segregateFullName
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReviewScannedDetailsViewModel @Inject constructor(
    val addedCardsRepository: AddedCardsRepository,
    val personalCardsRepository: PersonalCardsRepository,
    val userRepository: UserRepository,
    val dataStore: DataStore<Preferences>,
    val moshi: Moshi,
    val sessionManagerService: SessionManagerService
) : BaseViewModel() {

    lateinit var SCAN_TYPE: String

    var card = MutableLiveData(Card())

    var name = MutableLiveData(Name())
    var socials = MutableLiveData(
        mutableListOf<SocialMediaProfile>(
            SocialMediaProfile(type = SocialMediaProfile.SocialMedia.LinkedIn),
            SocialMediaProfile(type = SocialMediaProfile.SocialMedia.Facebook),
            SocialMediaProfile(type = SocialMediaProfile.SocialMedia.Twitter),
            SocialMediaProfile(type = SocialMediaProfile.SocialMedia.Instagram)
        )
    )

    var phoneNumbers = MutableLiveData(mutableSetOf(PhoneNumber()))
    var emailAddresses = MutableLiveData(mutableSetOf(EmailAddress()))

    var businessInfo = MutableLiveData(BusinessInfo())
    var note = MutableLiveData("")
    var cardNote = MutableLiveData("")

    var labelledToggle = MutableLiveData(true)
    var unlabelledToggle = MutableLiveData(true)

    var phoneOptionsToggle = MutableLiveData(false)
    var emailOptionsToggle = MutableLiveData(false)


    var unlabelledStrings = MutableLiveData(mutableListOf<String>())
    var labelledStrings = MutableLiveData(mutableListOf<LabelDetail>())

    val selectedDetail = MutableLiveData("")
    val selectedLabelType = MutableLiveData("")
    val selectedLabel = MutableLiveData(Utils.UNLABELLED)

    fun toggleUnLabelledVisibility() {
        unlabelledToggle.value = !unlabelledToggle.value!!
    }

    fun toggleLabelledVisibility() {
        labelledToggle.value = !labelledToggle.value!!
    }

    fun togglePhoneOptionsVisibility() {
        phoneOptionsToggle.value = !phoneOptionsToggle.value!!
/*        if(emailOptionsToggle.value!!)
            toggleEmailOptionsVisibility()*/

    }

    fun toggleEmailOptionsVisibility() {
        emailOptionsToggle.value = !emailOptionsToggle.value!!
/*        if(phoneOptionsToggle.value!!)
            togglePhoneOptionsVisibility()*/
    }

    fun retrieveScannedDetails(
        details: Array<String>, numberParentLabel: String,
        emailParentLabel: String, websiteLabel: String
    ) {

        val mutableDetails = details.toMutableList()

        details.forEach {
            if (Patterns.PHONE.matcher(it.replace(" ", "")).matches()) {
                labelledStrings.value?.add(
                    LabelDetail(
                        PhoneNumber.PhoneNumberType.Mobile.name,
                        it, numberParentLabel
                    )
                )
                mutableDetails.remove(it)
            }
        }

        details.forEach {
            if (Patterns.EMAIL_ADDRESS.matcher(it.replace(" ", "")).matches()) {
                labelledStrings.value?.add(
                    LabelDetail(EmailAddress.EmailType.Personal.name, it, emailParentLabel)
                )
                mutableDetails.remove(it)
            }

        }

        details.forEach {
            if (Patterns.WEB_URL.matcher(it.trim()).matches()) {
                labelledStrings.value?.add(
                    LabelDetail(
                        websiteLabel,
                        it
                    )
                )
                mutableDetails.remove(it)
            }
        }


        unlabelledStrings.value = mutableDetails
        unlabelledStrings.notifyObserver()
    }

    fun removeDetail(item: String) {
        unlabelledStrings.value?.remove(item)
        unlabelledStrings.notifyObserver()
    }

    fun editDetail(item: String) {
        selectedDetail.value = item
        _destination.value =
            Event(ReviewScannedDetailsFragmentDirections.actionReviewScannedDetailsFragmentToEditDetailFragment())

    }

    fun removeLabelledDetail(item: LabelDetail) {
        labelledStrings.value?.remove(item)
        labelledStrings.notifyObserver()
    }

    fun addUnLabelledDetail(item: String) {
        unlabelledStrings.value?.add(item)
        unlabelledStrings.notifyObserver()
    }

    fun addLabelledDetail(label: String, existingLabelDetail: LabelDetail?) {
        selectedLabel.value = label
        labelledStrings.value?.add(
            LabelDetail(
                label,
                selectedDetail.value!!,
                selectedLabelType.value!!
            )
        )
        if (existingLabelDetail != null)//its a swap
            removeLabelledDetail(existingLabelDetail)
        else
            removeDetail(selectedDetail.value!!)

        labelledStrings.notifyObserver()

    }

    fun updateLists() {
        card.value?.emailAddresses =
            emailAddresses.value?.toSet()?.filter { it.address.trim().isNotEmpty() }!!
        card.value?.phoneNumbers =
            phoneNumbers.value?.toSet()?.filter { it.number.trim().isNotEmpty() }!!

    }

    fun addLabelledDetail() {
        selectedDetail.value = ""
        selectedLabelType.value = ""
        selectedLabel.value = Utils.UNLABELLED
        _destination.value =
            Event(ReviewScannedDetailsFragmentDirections.actionReviewScannedDetailsFragmentToAddLabelledDetailFragment())

    }

    fun executeSwap(item: LabelDetail) {

        selectedDetail.value = item.detail
        selectedLabel.value = item.label
        selectedLabelType.value = item.parentLabel

        _destination.value = Event(
            ReviewScannedDetailsFragmentDirections.actionReviewScannedDetailsFragmentToAssignLabelsFragment(
                item
            )
        )

    }

    fun showLabelsFromNewDetail() {
        _destination.value =
            Event(AddLabelledDetailFragmentDirections.actionAddLabelledDetailFragmentToEditLabelsFragment())
    }

    fun showLabelsFromEditDetail() {
        _destination.value =
            Event(EditDetailFragmentDirections.actionEditDetailFragmentToEditLabelsFragment())
    }

    fun executeEdit(item: LabelDetail) {
        selectedDetail.value = item.detail
        selectedLabel.value = item.label
        selectedLabelType.value = item.parentLabel

        _destination.value = Event(
            ReviewScannedDetailsFragmentDirections.actionReviewScannedDetailsFragmentToEditDetailFragment(
                item
            )
        )

    }

    fun executeRemoveLabel(item: LabelDetail) {
        addUnLabelledDetail(item.detail)
        removeLabelledDetail(item)
    }

    fun showDialogIfNotDismissed() {
        viewModelScope.launch {
            dataStore.data.collect {
                val showDialog = if (SCAN_TYPE == Utils.SCAN_TYPE_ADDED)
                    it[Utils.ADDED_CARD_SCAN_ALERT] ?: true
                else
                    it[Utils.LIVE_CARD_SCAN_ALERT] ?: true

                if (showDialog)
                    _destination.postValue(Event(TakeCardPictureFragmentDirections.actionTakeCardPictureFragmentToScanAlertDialogFragment()))
            }
        }

    }

    fun setDialogDismissed() {
        viewModelScope.launch {
            dataStore.edit {
                if (SCAN_TYPE == Utils.SCAN_TYPE_ADDED)
                    it[Utils.ADDED_CARD_SCAN_ALERT] = false
                else
                    it[Utils.LIVE_CARD_SCAN_ALERT] = false
            }
        }

    }

    fun completeCreateCard() {
        card.value?.name?.segregateFullName()
        card.notifyObserver()

        when (SCAN_TYPE) {
            Utils.SCAN_TYPE_LIVE -> {
                card.value?.let {
                    val personalCard = LiveCard(it)
                    personalCard.owner = sessionManagerService.loggedInUserId()
                    addPersonalCard(personalCard)
                }

            }
            Utils.SCAN_TYPE_ADDED -> {
                card.value?.let {
                    val addedCard = AddedCard(it)
                    addedCard.note = cardNote.value
                    addCard(addedCard)
                }
            }
        }
    }

    fun addPersonalCard(personalCard: LiveCard) {
        viewModelScope.launch {
            card.value?.let {
                when (val data =
                    personalCardsRepository.firebaseLiveCardDataSource.addData(personalCard)) {
                    is Resource.Success -> {
                        //todo hide loader
                        //card.value?.id = data.data!!

                        _snackbarInt.postValue(Event(R.string.success))

                        userRepository.userDataSourceImpl.getData(null)
                            .catch {
                                emit(Resource.Error(R.id.error))
                            }.collect {
                                when (it) {
                                    is Resource.Success -> {
                                        if (it.data.name.isNullOrEmpty()) {
                                            dataStore.edit { mutablePrefs->
                                                card.value?.let { card->
                                                    mutablePrefs[Utils.NEW_USER_DISPLAY_NAME]= card.name.fullName
                                                    //we could pass it as a param to the destination, but what if the flow is interrupted
                                                    //the card is already created, we can just save the user name to shared prefs
                                                    _destination.postValue(Event(ScanNavDirections.actionGlobalSetUpAccountFragment()))
                                                }
                                            }
                                        } else {
                                            _destination.postValue(Event(ScanNavDirections.actionGlobalMeFragment()))
                                        }
                                    }
                                    is Resource.Loading -> {
                                        //todo show loader
                                    }
                                    is Resource.Error -> {
                                        Timber.d("addPersonalCard: ${it.errorCode}")
                                    }
                                }
                            }
                        data.data?.let {
                            card.value?.id = it
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


    fun addCard(addedCard: AddedCard) {

        viewModelScope.launch {
            card.value?.let {
                when (val data =
                    addedCardsRepository.firebaseAddedCardDataSource.addData(addedCard)) {
                    is Resource.Success -> {
                        //todo hide loader

                        _snackbarInt.postValue(Event(R.string.success))

                        _destination.postValue(
                            Event(
                                ScanNavDirections.actionGlobalScanNavToCardDetailsFragment(
                                    addedCard
                                )
                            )
                        )


                        data.data?.let {
                            card.value?.id = it
                        }
                        //updateBusinessLogo(data.data)

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

    companion object {
        private const val TAG = "ReviewScannedDetailsViewModel"
    }
}
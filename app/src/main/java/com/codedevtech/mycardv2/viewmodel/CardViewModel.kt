package com.codedevtech.mycardv2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.codedevtech.mycardv2.db.dao.AddedCardDao
import com.codedevtech.mycardv2.db.dao.LiveCardDao
import com.codedevtech.mycardv2.di.PersonalCardDataSource
import com.codedevtech.mycardv2.event.Event
import com.codedevtech.mycardv2.fragments.dashboard.CardOptionsFragmentDirections
import com.codedevtech.mycardv2.fragments.dashboard.CardsFragmentDirections
import com.codedevtech.mycardv2.fragments.dashboard.DeleteCardDialogFragmentDirections
import com.codedevtech.mycardv2.fragments.dashboard.MeFragmentDirections
import com.codedevtech.mycardv2.models.AddedCard
import com.codedevtech.mycardv2.models.LiveCard
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.models.datasource.AddedCardDataSource
import com.codedevtech.mycardv2.models.datasource.FirebaseAddedCardDataSourceImpl
import com.codedevtech.mycardv2.models.datasource.LiveCardDataSource
import com.codedevtech.mycardv2.repositories.AddedCardsRepository
import com.codedevtech.mycardv2.repositories.PersonalCardsRepository
import com.codedevtech.mycardv2.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(addedCardsRepository: AddedCardsRepository,
                                        personalCardsRepository: PersonalCardsRepository,
                                        val addedCardDataSourceImpl: AddedCardDataSource,
                                        val auth: FirebaseAuth,
                                        val personalCardDataSource: LiveCardDataSource,
                                        val addedCardDao: AddedCardDao,
                                        val liveCardDao: LiveCardDao): BaseViewModel() {

    val sortMode = MutableLiveData(Utils.SORT_MODE_RECENT)


    var selectedCard = MutableLiveData<AddedCard>()
    var selectedPersonalCard = MutableLiveData<LiveCard>()

    val cardsLiveData = Transformations.switchMap(sortMode) {
        return@switchMap when(it){
            Utils.SORT_MODE_NAME -> addedCardsRepository.getSortedCards("name.fullName").asLiveData()
            Utils.SORT_MODE_RECENT-> addedCardsRepository.getSortedCards("createdAt").asLiveData()
            else -> addedCardsRepository.getSortedCards("createdAt").asLiveData()
        }
    }

    val personalCardsLiveData =  personalCardsRepository.getPersonalCards(auth.currentUser!!.uid).asLiveData()

    fun deleteCard(){
        selectedCard.value?.let {
            viewModelScope.launch {
                when(val deleteData = addedCardDataSourceImpl.removeData(it)){
                    is Resource.Success->{
                        _destination.value =(Event(CardsFragmentDirections.actionGlobalCardsFragment()))
                    }
                    is Resource.Error-> _snackbarInt.postValue(Event(deleteData.errorCode))
                }
            }

        }

    }

    fun deletePersonalCard(){

        selectedPersonalCard.value?.let {
            viewModelScope.launch {
                when(val deleteData = personalCardDataSource.removeData(it)){
                    is Resource.Success->{
                        _destination.value = (Event(MeFragmentDirections.actionGlobalMeFragment()))
                    }
                    is Resource.Error-> _snackbarInt.postValue(Event(deleteData.errorCode))
                }
            }

        }

    }

    fun save(data: List<AddedCard>) {
        //todo find a way to move this to the get call
        viewModelScope.launch (Dispatchers.IO){
            addedCardDao.deleteAll()
            addedCardDao.addAllCards(data)
        }
    }

    fun savePersonal(data: List<LiveCard>) {
        //todo find a way to move this to the get call
        viewModelScope.launch (Dispatchers.IO){
            liveCardDao.deleteAll()
            liveCardDao.addAllCards(data)
        }
    }
        //val cardsPagedDataFlow = cardsRepository.allCardsPaged.flow.cachedIn(viewModelScope)

        //val cardsLiveDataFlow = cardsRepository.currentPage


/*    val pagedSort = Transformations.map(sortMode, Function {
        cardsRepository.firestoreCardPagingSource.filterBy(it)
    })*/


}

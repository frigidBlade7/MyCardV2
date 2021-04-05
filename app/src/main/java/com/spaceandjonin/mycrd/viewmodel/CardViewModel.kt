package com.spaceandjonin.mycrd.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.spaceandjonin.mycrd.db.dao.AddedCardDao
import com.spaceandjonin.mycrd.db.dao.LiveCardDao
import com.spaceandjonin.mycrd.event.Event
import com.spaceandjonin.mycrd.fragments.dashboard.CardsFragmentDirections
import com.spaceandjonin.mycrd.models.AddedCard
import com.spaceandjonin.mycrd.models.LiveCard
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.models.datasource.AddedCardDataSource
import com.spaceandjonin.mycrd.models.datasource.LiveCardDataSource
import com.spaceandjonin.mycrd.repositories.AddedCardsRepository
import com.spaceandjonin.mycrd.repositories.PersonalCardsRepository
import com.spaceandjonin.mycrd.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.spaceandjonin.mycrd.di.VcfFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(addedCardsRepository: AddedCardsRepository,
                                        personalCardsRepository: PersonalCardsRepository,
                                        val addedCardDataSourceImpl: AddedCardDataSource,
                                        val auth: FirebaseAuth,
                                        val storageDir: File?,
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

/* todo get pending list   val exampleCounterFlow: Flow<List<String>> = dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[Utils.PENDING_URI_LIST]. ?: listOf()
        }*/

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
                        //_destination.postValue(Event(MeFragmentDirections.actionGlobalMeFragment()))
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

    fun createVcf(name: String?): File?{
            return File(storageDir,"$name.vcf")
    }
    //val cardsPagedDataFlow = cardsRepository.allCardsPaged.flow.cachedIn(viewModelScope)

        //val cardsLiveDataFlow = cardsRepository.currentPage


/*    val pagedSort = Transformations.map(sortMode, Function {
        cardsRepository.firestoreCardPagingSource.filterBy(it)
    })*/


}

package com.codedevtech.mycardv2.viewmodel

import androidx.lifecycle.*
import androidx.paging.*
import com.codedevtech.mycardv2.models.AddedCard
import com.codedevtech.mycardv2.repositories.AddedCardsRepository
import com.codedevtech.mycardv2.repositories.PersonalCardsRepository
import com.codedevtech.mycardv2.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.switchMap
import javax.inject.Inject

@HiltViewModel
class SearchCardViewModel @Inject constructor(val addedCardsRepository: AddedCardsRepository): BaseViewModel() {


    val searchMode = MutableLiveData(Utils.FILTER_ALL)
    val filterQuery = MutableLiveData("")


    var pagedAddedCardsFlow = Transformations.switchMap(DoubleTransformation(searchMode,filterQuery)){
        when(it.first){
            Utils.FILTER_COMPANY-> Pager(PagingConfig(10)){
                addedCardsRepository.addedCardDao.filterByCompany("%${it.second}%")}.flow.cachedIn(viewModelScope).asLiveData()
            Utils.FILTER_ROLE -> Pager(PagingConfig(10)) {
                addedCardsRepository.addedCardDao.filterByRole("%${it.second}%")
            }.flow.cachedIn(viewModelScope).asLiveData()
            Utils.FILTER_NAME -> Pager(PagingConfig(10)) {
                addedCardsRepository.addedCardDao.filterByName("%${it.second}%")
            }.flow.cachedIn(viewModelScope).asLiveData()
            Utils.FILTER_ALL -> Pager(PagingConfig(10)) {
                addedCardsRepository.addedCardDao.filterByAny("*${it.second}*")
            }.flow.cachedIn(viewModelScope).asLiveData()
            else -> Pager(PagingConfig(10)){
                addedCardsRepository.addedCardDao.filterByAny("%${it.second}%") }.flow.cachedIn(viewModelScope).asLiveData()
        }
    }


    //clever little use of Pairs and MediatorLiveData

    class DoubleTransformation<A, B>(a: LiveData<A>, b: LiveData<B>) : MediatorLiveData<Pair<A?, B?>>() {
        init {
            addSource(a) { value = it to b.value }
            addSource(b) { value = a.value to it }
        }
    }

}

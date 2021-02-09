package com.codedevtech.mycardv2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bumptech.glide.load.engine.Resource
import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.repositories.CardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(cardsRepository: CardsRepository): BaseViewModel() {

    val cardsLiveData = cardsRepository.allCards.asLiveData()

    val cardsLiveDataPaged = cardsRepository.allCardsPaged.flow.cachedIn(viewModelScope)

}

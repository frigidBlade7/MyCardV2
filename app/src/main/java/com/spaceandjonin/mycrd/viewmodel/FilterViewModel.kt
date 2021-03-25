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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(): BaseViewModel() {
    val sortMode = MutableLiveData(Utils.SORT_MODE_RECENT)
}

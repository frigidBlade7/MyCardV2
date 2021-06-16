package com.spaceandjonin.mycrd.viewmodel

import androidx.lifecycle.MutableLiveData
import com.spaceandjonin.mycrd.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(): BaseViewModel() {
    val sortMode = MutableLiveData(Utils.SORT_MODE_RECENT)
}

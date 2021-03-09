package com.codedevtech.mycardv2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.codedevtech.mycardv2.event.Event

open class BaseViewModel : ViewModel() {


    val _destination = MutableLiveData<Event<NavDirections>>()
    val _snackbarInt = MutableLiveData<Event<Int>>()
    val _snackbarString = MutableLiveData<Event<String>>()
    val _loaderVisibility = MutableLiveData<Event<Boolean>>()


    val destination: LiveData<Event<NavDirections>>
        get() = _destination


    val snackbarInt: LiveData<Event<Int>>
        get() = _snackbarInt


    val snackbarString: LiveData<Event<String>>
        get() = _snackbarString


    val loaderVisibility: LiveData<Event<String>>
        get() = _snackbarString


}
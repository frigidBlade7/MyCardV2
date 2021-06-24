package com.spaceandjonin.mycrd.models

sealed class Resource<out T> {

    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val errorCode: Int/*, val cacheData: T*/) : Resource<Nothing>()

}

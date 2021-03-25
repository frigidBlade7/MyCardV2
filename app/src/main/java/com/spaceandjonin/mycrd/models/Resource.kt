package com.spaceandjonin.mycrd.models

sealed class Resource<out T>{

    //var code: Int = 0

    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T): Resource<T>()
    data class Error(val errorCode: Int/*, val cacheData: T*/): Resource<Nothing>()


}

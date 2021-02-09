package com.codedevtech.mycardv2.models

import kotlinx.coroutines.flow.Flow

//R is return type, T is data type
interface DataSource<T> {

    suspend fun addData(data: T): Resource<String>

    suspend fun removeData(data: T): Resource<String>

    suspend fun updateData(data: T): Resource<String>

    fun getData(id: String): Flow<Resource<T>>

    fun getList(): Flow<Resource<List<T>>>

}
package com.codedevtech.mycardv2.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.codedevtech.mycardv2.models.PhoneNumber
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

@ProvidedTypeConverter
    class PhoneNumberConverter @Inject constructor(var moshi: Moshi) {

        val dataListType =
            Types.newParameterizedType(MutableList::class.java, PhoneNumber::class.java)
        val dataListAdapter: JsonAdapter<List<PhoneNumber>> = moshi.adapter(dataListType)


        @TypeConverter
        fun listToDataJson(value: List<PhoneNumber>) = dataListAdapter.toJson(value)

        @TypeConverter
        fun dataJsonToList(value: String) = dataListAdapter.fromJson(value)
    }
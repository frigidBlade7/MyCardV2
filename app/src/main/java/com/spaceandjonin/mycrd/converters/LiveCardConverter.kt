package com.spaceandjonin.mycrd.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.spaceandjonin.mycrd.models.SocialMediaProfile
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

@ProvidedTypeConverter
class LiveCardConverter @Inject constructor(val moshi: Moshi) {

    val dataListType =
        Types.newParameterizedType(MutableList::class.java, SocialMediaProfile::class.java)
    val dataListAdapter: JsonAdapter<List<SocialMediaProfile>> = moshi.adapter(dataListType)


    @TypeConverter
    fun listToDataJson(value: List<SocialMediaProfile>) = dataListAdapter.toJson(value)

    @TypeConverter
    fun dataJsonToList(value: String) = dataListAdapter.fromJson(value)
}
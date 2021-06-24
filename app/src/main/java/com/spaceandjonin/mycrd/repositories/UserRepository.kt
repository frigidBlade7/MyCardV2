package com.spaceandjonin.mycrd.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.spaceandjonin.mycrd.models.Card
import com.spaceandjonin.mycrd.models.CardJsonAdapter
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.models.User
import com.spaceandjonin.mycrd.models.datasource.UserDataSourceImpl
import com.spaceandjonin.mycrd.utils.Utils
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(
    val dataStore: DataStore<Preferences>,
    val moshi: Moshi, val userDataSourceImpl: UserDataSourceImpl
) {


    val cardJsonFlow: Flow<Card?> = dataStore.data.catch { exception ->
        when (exception) {
            is IOException -> emit(emptyPreferences())
            else -> Timber.d( exception.localizedMessage!!)
        }
    }.map {
        val data = it[Utils.NEW_USER_LIVE_CARD] ?: ""
        if (data.isNotEmpty())
            CardJsonAdapter(moshi).fromJson(data)
        else
            null
    }

    fun getLoggedInUser(): LiveData<Resource<User>> {
        return userDataSourceImpl.getData(null).asLiveData()
    }
}
package com.spaceandjonin.mycrd.repositories

import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.models.LiveCard
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.models.datasource.LiveCardDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "PersonalCardsRepository"

class PersonalCardsRepository @Inject constructor(val firebaseLiveCardDataSource: LiveCardDataSource/*, val firestoreCardPagingSource: FirestoreCardPagingSource*/) {

    fun getPersonalCards(owner: String): Flow<Resource<List<LiveCard>>> {
        return firebaseLiveCardDataSource.getList(owner).catch { exception ->
            Timber.d( "error: ${exception.localizedMessage}")
            emit(Resource.Error(R.string.failed))
        }//todo add a catch for others
    }

    fun getFirstPersonalCard(owner: String): Flow<Resource<LiveCard>> {
        return firebaseLiveCardDataSource.getFirst(owner).catch { exception ->
            Timber.d( "error: ${exception.localizedMessage}")
            emit(Resource.Error(R.string.failed))
        }//todo add a catch for others
    }
}
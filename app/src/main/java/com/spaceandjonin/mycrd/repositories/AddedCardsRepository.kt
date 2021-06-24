package com.spaceandjonin.mycrd.repositories

import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.db.dao.AddedCardDao
import com.spaceandjonin.mycrd.models.AddedCard
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.models.datasource.AddedCardDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import javax.inject.Inject

class AddedCardsRepository @Inject constructor(
    val firebaseAddedCardDataSource: AddedCardDataSource,
    val addedCardDao: AddedCardDao) {


    fun getSortedCards(sort: String): Flow<Resource<List<AddedCard>>> {
        return firebaseAddedCardDataSource.getList(sort).catch { exception ->
            Timber.d( "error: ${exception.localizedMessage}")
            emit(Resource.Error(R.string.failed))
        }
    }

}
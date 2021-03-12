package com.spaceandjonin.mycrd.repositories

import android.util.Log
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.db.dao.AddedCardDao
import com.spaceandjonin.mycrd.models.AddedCard
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.models.datasource.AddedCardDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class AddedCardsRepository @Inject constructor(val firebaseAddedCardDataSource: AddedCardDataSource, val addedCardDao: AddedCardDao/*, val firestoreCardPagingSource: FirestoreCardPagingSource*/) {


        fun getSortedCards(sort: String): Flow<Resource<List<AddedCard>>> {
            return firebaseAddedCardDataSource.getList(sort).catch { exception ->
                Log.d(TAG, "error: ${exception.localizedMessage}")
                emit(Resource.Error(R.string.failed))
            }
        }

/*    val addedCardsPagedByAny = Pager(PagingConfig(10)){
        addedCardDao.filterByAny()
    }.flow



    val addedCardsPagedByName = Pager(PagingConfig(10)){
        addedCardDao.filterByName("")
    }

    val addedCardsPagedByCompany = Pager(PagingConfig(10)){
        addedCardDao.filterByCompany("")
    }

    val addedCardsPagedByRole = Pager(PagingConfig(10)){
        addedCardDao.filterByRole("")
    }*/
/*
    val currentPage = firestoreCardPagingSource.currentQuery
*/


/*    fun sort(sortString: String){
        firestoreCardPagingSource.filterBy(sortString)
    }*/
/*    suspend fun addCard(card: Card?){
        card?.let {
            firebaseCardDataSource.addData(it)
        }
    } */

    companion object {
        private const val TAG = "CardsRepository"
    }

}
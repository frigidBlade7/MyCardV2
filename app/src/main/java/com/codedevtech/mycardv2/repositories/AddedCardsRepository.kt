package com.codedevtech.mycardv2.repositories

import android.util.Log
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.db.dao.AddedCardDao
import com.codedevtech.mycardv2.models.AddedCard
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.models.datasource.AddedCardDataSource
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
package com.codedevtech.mycardv2.repositories

import android.util.Log
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.di.PersonalCardDataSource
import com.codedevtech.mycardv2.models.*
import com.codedevtech.mycardv2.models.datasource.LiveCardDataSource
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class PersonalCardsRepository @Inject constructor(val firebaseLiveCardDataSource: LiveCardDataSource/*, val firestoreCardPagingSource: FirestoreCardPagingSource*/) {

    val allCards = firebaseLiveCardDataSource.getList().catch { exception ->
        Log.d(TAG, "error: ${exception.localizedMessage}")
        emit(Resource.Error(R.string.failed))
    }

/*    val allCardsPaged = Pager(PagingConfig(10)){
        firestoreCardPagingSource
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
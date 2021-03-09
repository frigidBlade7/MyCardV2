package com.codedevtech.mycardv2.repositories

import android.util.Log
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.models.LiveCard
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.models.datasource.LiveCardDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

private const val TAG = "PersonalCardsRepository"

class PersonalCardsRepository @Inject constructor(val firebaseLiveCardDataSource: LiveCardDataSource/*, val firestoreCardPagingSource: FirestoreCardPagingSource*/) {

    fun getPersonalCards(owner: String): Flow<Resource<List<LiveCard>>> {
        return firebaseLiveCardDataSource.getList(owner).catch { exception ->
            Log.d(TAG, "error: ${exception.localizedMessage}")
            emit(Resource.Error(R.string.failed))
        }//todo add a catch for others
    }
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

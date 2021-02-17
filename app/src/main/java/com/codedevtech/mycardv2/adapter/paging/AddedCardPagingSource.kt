package com.codedevtech.mycardv2.adapter.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.codedevtech.mycardv2.db.dao.AddedCardDao
import com.codedevtech.mycardv2.models.AddedCard
import com.codedevtech.mycardv2.models.LiveCard
import com.codedevtech.mycardv2.utils.Utils
import com.codedevtech.mycardv2.utils.awaitContinuous
import com.google.firebase.firestore.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


private const val TAG = "FirestoreCardPagingSour"
class AddedCardPagingSource @Inject constructor(val addedCardDao: AddedCardDao):
    PagingSource<Int, AddedCard>() {


    override fun getRefreshKey(state: PagingState<Int, AddedCard>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AddedCard> {
        TODO()
        /*return try {

            val currentPageQuery = params.key?: addedCardDao.filterByName("%%").limit(Utils.PAGE_SIZE).get().await()
            val lastDocument = currentPageQuery.documents.last()
            val nextPageQuery = collectionReference.limit(Utils.PAGE_SIZE).startAfter(lastDocument).get().await()


            var data = currentPageQuery.toObjects(LiveCard::class.java)

            if(data.size <Utils.PAGE_SIZE){
                //added listener
                Log.d(TAG, "load:update query state flow")
                currentQuery.value= currentPageQuery.query
            }

            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1


            val response = addedCardDao.addAllCards()
            LoadResult.Page(
                data = response.,
                prevKey = null, // Only paging forward.
                nextKey = response.nextPageNumber
            )
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            Log.d(TAG, "load: ${e.localizedMessage}")
            LoadResult.Error(e)
        }*/
    }
}

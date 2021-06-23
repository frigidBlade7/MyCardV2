package com.spaceandjonin.mycrd.adapter.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.spaceandjonin.mycrd.db.dao.AddedCardDao
import com.spaceandjonin.mycrd.models.AddedCard
import javax.inject.Inject


private const val TAG = "FirestoreCardPagingSour"

class AddedCardPagingSource @Inject constructor(val addedCardDao: AddedCardDao) :
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
                Timber.d( "load:update query state flow")
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
            Timber.d( "load: ${e.localizedMessage}")
            LoadResult.Error(e)
        }*/
    }
}

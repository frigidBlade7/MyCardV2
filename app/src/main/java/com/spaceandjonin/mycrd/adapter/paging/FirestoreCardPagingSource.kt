package com.spaceandjonin.mycrd.adapter.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.spaceandjonin.mycrd.models.LiveCard
import com.spaceandjonin.mycrd.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject


private const val TAG = "FirestoreCardPagingSour"

class FirestoreCardPagingSource @Inject constructor(db: FirebaseFirestore) :
    PagingSource<QuerySnapshot, LiveCard>() {

    private val collectionReference = db.collection("cards").orderBy("name.fullName")

    val currentQuery = MutableStateFlow<Query?>(null)
    override fun getRefreshKey(state: PagingState<QuerySnapshot, LiveCard>): QuerySnapshot? {
        TODO("Not yet implemented")
    }

    fun filterBy(sort: String): PagingSource<QuerySnapshot, LiveCard> {
        when (sort) {
            Utils.SORT_MODE_NAME -> collectionReference.orderBy("name.fullname")
            Utils.SORT_MODE_RECENT -> collectionReference.orderBy("timestamp")
        }
        return this
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, LiveCard> {

        return try {


            val currentPageQuery =
                params.key ?: collectionReference.limit(Utils.PAGE_SIZE).get().await()
            val lastDocument = currentPageQuery.documents.last()
            val nextPageQuery =
                collectionReference.limit(Utils.PAGE_SIZE).startAfter(lastDocument).get().await()


            val data = currentPageQuery.toObjects(LiveCard::class.java)

            if (data.size < Utils.PAGE_SIZE) {
                //added listener
                Timber.d( "load:update query state flow")
                currentQuery.value = currentPageQuery.query
            }

            LoadResult.Page(data, null, nextPageQuery)


        } catch (e: Exception) {
            Timber.d( "load: ${e.localizedMessage}")
            LoadResult.Error(e)
        }
    }
}

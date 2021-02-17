package com.codedevtech.mycardv2.adapter.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
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
class FirestoreCardPagingSource @Inject constructor(db: FirebaseFirestore):
    PagingSource<QuerySnapshot, LiveCard>() {

    private val collectionReference = db.collection("cards").orderBy("name.fullName")

    val currentQuery = MutableStateFlow<Query?>(null)
    override fun getRefreshKey(state: PagingState<QuerySnapshot, LiveCard>): QuerySnapshot? {
        TODO("Not yet implemented")
    }

    fun filterBy(sort: String): PagingSource<QuerySnapshot,LiveCard> {
        when(sort){
            Utils.SORT_MODE_NAME-> collectionReference.orderBy("name.fullname")
            Utils.SORT_MODE_RECENT -> collectionReference.orderBy("timestamp")
        }
        return this
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, LiveCard> {

        return try {


            val currentPageQuery = params.key?: collectionReference.limit(Utils.PAGE_SIZE).get().await()
            val lastDocument = currentPageQuery.documents.last()
            val nextPageQuery = collectionReference.limit(Utils.PAGE_SIZE).startAfter(lastDocument).get().await()


            var data = currentPageQuery.toObjects(LiveCard::class.java)

            if(data.size <Utils.PAGE_SIZE){
                //added listener
                Log.d(TAG, "load:update query state flow")
                currentQuery.value= currentPageQuery.query
            }

            LoadResult.Page(data,null, nextPageQuery)


        }catch (e: Exception){
            Log.d(TAG, "load: ${e.localizedMessage}")
            LoadResult.Error(e)
        }
    }
}

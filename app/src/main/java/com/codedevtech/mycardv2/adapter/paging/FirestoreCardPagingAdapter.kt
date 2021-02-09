package com.codedevtech.mycardv2.adapter.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.utils.Utils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


private const val TAG = "FirestoreCardPagingAdap"
class FirestoreCardPagingAdapter @Inject constructor(db: FirebaseFirestore):
    PagingSource<QuerySnapshot, Card>() {

    private val collectionReference = db.collection("cards")/*.orderBy("timestamp")*/

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Card>): QuerySnapshot? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Card> {

        return try {


            val currentPageQuery = params.key?: collectionReference.limit(Utils.PAGE_SIZE).get().await()
            val lastDocument = currentPageQuery.documents.last()
            val nextPageQuery = collectionReference.limit(Utils.PAGE_SIZE).startAfter(lastDocument).get().await()

            LoadResult.Page(currentPageQuery.toObjects(Card::class.java),null,
            nextPageQuery)


        }catch (e: Exception){
            Log.d(TAG, "load: ${e.localizedMessage}")
            LoadResult.Error(e)
        }
    }
}
package com.codedevtech.mycardv2.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.paging.FirestoreCardPagingAdapter
import com.codedevtech.mycardv2.di.AnnotationQualifiers
import com.codedevtech.mycardv2.models.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Flow
import javax.inject.Inject

class CardsRepository @Inject constructor(val firebaseCardDataSource: CardDataSource, firestoreCardPagingAdapter: FirestoreCardPagingAdapter) {

    val allCards = firebaseCardDataSource.getList().catch { exception ->
        Log.d(TAG, "error: ${exception.localizedMessage}")
        emit(Resource.Error(R.string.failed))
    }

    val allCardsPaged = Pager(PagingConfig(10)){
        firestoreCardPagingAdapter
    }

/*    suspend fun addCard(card: Card?){
        card?.let {
            firebaseCardDataSource.addData(it)
        }
    } */

    companion object {
        private const val TAG = "CardsRepository"
    }

}
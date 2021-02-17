package com.codedevtech.mycardv2.models.datasource

import android.util.Log
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.db.dao.AddedCardDao
import com.codedevtech.mycardv2.models.AddedCard
import com.codedevtech.mycardv2.models.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

private const val TAG = "FirebaseAddedCardDataSo"
class FirebaseAddedCardDataSourceImpl(override var collectionReference: CollectionReference, val addedCardDao: AddedCardDao) : AddedCardDataSource(){


}

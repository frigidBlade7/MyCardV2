package com.codedevtech.mycardv2.models.datasource

import com.codedevtech.mycardv2.db.dao.AddedCardDao
import com.google.firebase.firestore.CollectionReference

private const val TAG = "FirebaseAddedCardDataSo"
class FirebaseAddedCardDataSourceImpl(override var collectionReference: CollectionReference) : AddedCardDataSource(){


}

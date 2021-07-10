package com.spaceandjonin.mycrd.models.datasource

import com.google.firebase.firestore.CollectionReference

private const val TAG = "FirebaseAddedCardDataSo"

class FirebaseAddedCardDataSourceImpl(override val collectionReference: CollectionReference) :
    AddedCardDataSource() {


}

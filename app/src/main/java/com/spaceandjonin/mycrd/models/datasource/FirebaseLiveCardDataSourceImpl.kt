package com.spaceandjonin.mycrd.models.datasource

import com.google.firebase.firestore.CollectionReference

class FirebaseLiveCardDataSourceImpl(override var collectionReference: CollectionReference) :
    LiveCardDataSource()


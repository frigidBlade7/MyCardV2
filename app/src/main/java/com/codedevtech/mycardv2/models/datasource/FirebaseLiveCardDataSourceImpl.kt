package com.codedevtech.mycardv2.models.datasource

import com.google.firebase.firestore.CollectionReference

class FirebaseLiveCardDataSourceImpl(override var collectionReference: CollectionReference) : LiveCardDataSource()


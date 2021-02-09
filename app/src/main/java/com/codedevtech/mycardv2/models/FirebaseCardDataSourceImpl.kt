package com.codedevtech.mycardv2.models

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference

class FirebaseCardDataSourceImpl(override var collectionReference: CollectionReference) : CardDataSource()


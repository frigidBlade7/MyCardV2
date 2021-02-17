package com.codedevtech.mycardv2.models.datasource

import com.codedevtech.mycardv2.services.UpdateImageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseUserDataSourceImpl @Inject constructor(override var auth: FirebaseAuth) : UserDataSourceImpl()
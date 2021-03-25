package com.spaceandjonin.mycrd.models.datasource

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseUserDataSourceImpl @Inject constructor(override var auth: FirebaseAuth) : UserDataSourceImpl()
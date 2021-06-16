package com.spaceandjonin.mycrd.services

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseSessionManagerServiceImpl @Inject constructor(val firebaseAuth: FirebaseAuth): SessionManagerService {
    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun loggedInUserId(): String {
        firebaseAuth.currentUser?.let {
             return it.uid
        }
        return "john_doe_id" //todo temp fix until we use @AssistedInject
    }
}
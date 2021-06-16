package com.spaceandjonin.mycrd.services

interface SessionManagerService {
    fun isUserLoggedIn(): Boolean

    fun loggedInUserId(): String
}
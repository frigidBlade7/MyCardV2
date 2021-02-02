package com.codedevtech.mycardv2.di

import com.codedevtech.mycardv2.services.AuthenticationServiceImpl
import com.codedevtech.mycardv2.services.AuthenticationService
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Provides
    @Singleton
    fun providesAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }


/*    @Provides
    @Singleton
    fun providesAuthenticationService(auth: FirebaseAuth): AuthenticationService{
        return AuthenticationServiceImpl(auth)
    }*/

}







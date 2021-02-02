package com.codedevtech.mycardv2.di

import com.codedevtech.mycardv2.services.AuthenticationServiceImpl
import com.codedevtech.mycardv2.services.AuthenticationService
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)

object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun providesAuthenticationService(auth: FirebaseAuth): AuthenticationService{
        return AuthenticationServiceImpl(auth)
    }
}







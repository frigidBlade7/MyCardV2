package com.codedevtech.mycardv2.di

import com.codedevtech.mycardv2.models.CardDataSource
import com.codedevtech.mycardv2.models.FirebaseCardDataSourceImpl
import com.codedevtech.mycardv2.services.AuthenticationServiceImpl
import com.codedevtech.mycardv2.services.AuthenticationService
import com.codedevtech.mycardv2.services.UpdateImageService
import com.codedevtech.mycardv2.services.FirebaseUpdateImageServiceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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

    @Provides
    @ViewModelScoped
    fun providesFirebaseCardDataSource(db: FirebaseFirestore): CardDataSource {
        return FirebaseCardDataSourceImpl(db.collection("cards"))
    }

    @Provides
    @ViewModelScoped
    fun providesUpdateImageService(fs: FirebaseStorage): UpdateImageService{
        return FirebaseUpdateImageServiceImpl(fs)
    }
}







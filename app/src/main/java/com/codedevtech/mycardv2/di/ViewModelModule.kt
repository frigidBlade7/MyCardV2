package com.codedevtech.mycardv2.di

import com.codedevtech.mycardv2.db.AppDb
import com.codedevtech.mycardv2.db.dao.AddedCardDao
import com.codedevtech.mycardv2.models.*
import com.codedevtech.mycardv2.models.datasource.*
import com.codedevtech.mycardv2.models.datasource.AddedCardDataSource
import com.codedevtech.mycardv2.services.*
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
    @AuthService
    fun providesAuthenticationService(auth: FirebaseAuth): AuthenticationService{
        return AuthenticationServiceImpl(auth)
    }

    @Provides
    @ViewModelScoped
    @UpdateService
    fun providesUpdateNumberService(auth: FirebaseAuth): AuthenticationService{
        return UpdatePhoneNumberServiceImpl(auth)
    }

    @Provides
    @ViewModelScoped
    fun providesAddedCardDataSource(db: FirebaseFirestore, auth: FirebaseAuth, addedCardDao: AddedCardDao): AddedCardDataSource {
        return FirebaseAddedCardDataSourceImpl(db.collection("users")
            .document(auth.currentUser!!.uid).collection("addedCards"),addedCardDao)
    }

    @Provides
    @ViewModelScoped
    fun providesPersonalCardDataSource(db: FirebaseFirestore): LiveCardDataSource {
        return FirebaseLiveCardDataSourceImpl(db.collection("personalCards"))
    }


    @Provides
    @ViewModelScoped
    fun providesUserDataSource(auth: FirebaseAuth): UserDataSourceImpl {
        return FirebaseUserDataSourceImpl(auth)
    }

    @Provides
    @ViewModelScoped
    fun providesUpdateImageService(fs: FirebaseStorage): UpdateImageService{
        return FirebaseUpdateImageServiceImpl(fs)
    }

}







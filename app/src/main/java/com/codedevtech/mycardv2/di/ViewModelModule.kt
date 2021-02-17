package com.codedevtech.mycardv2.di

import com.codedevtech.mycardv2.db.AppDb
import com.codedevtech.mycardv2.db.dao.AddedCardDao
import com.codedevtech.mycardv2.models.*
import com.codedevtech.mycardv2.models.datasource.*
import com.codedevtech.mycardv2.models.datasource.AddedCardDataSource
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

    @Provides
    @ViewModelScoped
    fun providesAddedCardDao(appDb: AppDb): AddedCardDao{
        return appDb.addedCardsDao()
    }
}







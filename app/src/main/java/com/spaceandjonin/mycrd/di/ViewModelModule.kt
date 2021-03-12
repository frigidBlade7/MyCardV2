package com.spaceandjonin.mycrd.di

import com.spaceandjonin.mycrd.models.datasource.*
import com.spaceandjonin.mycrd.models.datasource.AddedCardDataSource
import com.spaceandjonin.mycrd.services.*
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
    fun providesAddedCardDataSource(db: FirebaseFirestore, auth: FirebaseAuth): AddedCardDataSource {
        return FirebaseAddedCardDataSourceImpl(db.collection("users")
            .document(auth.currentUser!!.uid).collection("addedCards"))
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







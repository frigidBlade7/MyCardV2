package com.spaceandjonin.mycrd.di

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

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


    @SuppressLint("SimpleDateFormat")
    @Provides
    @ViewModelScoped
    @ImageFile
    fun providesImageFile(@ApplicationContext applicationContext: Context): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "MYCRD_JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )/*.apply {
            // Save a file: path for use with ACTION_VIEW intents
            //  currentPhotoPath = absolutePath
        }*/
    }

}







package com.spaceandjonin.mycrd.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.work.WorkManager
import com.spaceandjonin.mycrd.converters.EmailAddressConverter
import com.spaceandjonin.mycrd.converters.PhoneNumberConverter
import com.spaceandjonin.mycrd.converters.SocialMediaProfileConverter
import com.spaceandjonin.mycrd.db.AppDb
import com.spaceandjonin.mycrd.db.dao.AddedCardDao
import com.spaceandjonin.mycrd.db.dao.LiveCardDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun providesMoshi():Moshi{
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun providesDb(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun providesFileStorage():FirebaseStorage{
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun providesLocalDb(@ApplicationContext context: Context, socialMediaProfileConverter: SocialMediaProfileConverter
                        ,phoneNumberConverter: PhoneNumberConverter,emailAddressConverter: EmailAddressConverter):AppDb{
        return AppDb.getDatabase(context, socialMediaProfileConverter, phoneNumberConverter, emailAddressConverter)
    }

    @Provides
    @Singleton
    fun providesNetworkInfo(@ApplicationContext context: Context): ConnectivityManager{
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }

    @Provides
    @Singleton
    fun providesLiveCardDao(appDb: AppDb): LiveCardDao{
        return appDb.liveCardsDao()
    }

    @Provides
    @Singleton
    fun providesAddedCardDao(appDb: AppDb): AddedCardDao {
        return appDb.addedCardsDao()
    }

    @Provides
    @Singleton
    fun providesWorkManager(@ApplicationContext applicationContext: Context): WorkManager{
        return WorkManager.getInstance(applicationContext)
    }

}







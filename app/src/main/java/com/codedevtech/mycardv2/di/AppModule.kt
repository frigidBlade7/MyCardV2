package com.codedevtech.mycardv2.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.codedevtech.mycardv2.converters.EmailAddressConverter
import com.codedevtech.mycardv2.converters.PhoneNumberConverter
import com.codedevtech.mycardv2.converters.SocialMediaProfileConverter
import com.codedevtech.mycardv2.db.AppDb
import com.codedevtech.mycardv2.db.dao.AddedCardDao
import com.codedevtech.mycardv2.db.dao.LiveCardDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

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
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
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

/*    @Provides
    @Singleton
    fun providesAuthenticationService(auth: FirebaseAuth): AuthenticationService{
        return AuthenticationServiceImpl(auth)
    }*/

}







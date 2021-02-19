package com.codedevtech.mycardv2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codedevtech.mycardv2.converters.EmailAddressConverter
import com.codedevtech.mycardv2.converters.PhoneNumberConverter
import com.codedevtech.mycardv2.converters.SocialMediaProfileConverter
import com.codedevtech.mycardv2.converters.TimestampConverter
import com.codedevtech.mycardv2.db.dao.AddedCardDao
import com.codedevtech.mycardv2.db.dao.LiveCardDao
import com.codedevtech.mycardv2.models.AddedCard
import com.codedevtech.mycardv2.models.LiveCard
import javax.inject.Inject

@Database(entities = [LiveCard::class, AddedCard::class], version = 2, exportSchema = false)

@TypeConverters(SocialMediaProfileConverter::class, PhoneNumberConverter::class, EmailAddressConverter::class, TimestampConverter::class )
abstract class AppDb : RoomDatabase() {

    abstract fun liveCardsDao(): LiveCardDao

    abstract fun addedCardsDao(): AddedCardDao


    companion object{


        @Volatile
        private var INSTANCE : AppDb? = null

        fun getDatabase(context: Context, socialMediaProfileConverter: SocialMediaProfileConverter,
                        phoneNumberConverter: PhoneNumberConverter, emailAddressConverter: EmailAddressConverter): AppDb {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance

            synchronized(this)
            {
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDb::class.java,"app_db")
                    .addTypeConverter(socialMediaProfileConverter)
                    .addTypeConverter(emailAddressConverter)
                    .addTypeConverter(phoneNumberConverter).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }

        }
    }
}
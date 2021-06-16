package com.spaceandjonin.mycrd.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spaceandjonin.mycrd.converters.EmailAddressConverter
import com.spaceandjonin.mycrd.converters.PhoneNumberConverter
import com.spaceandjonin.mycrd.converters.SocialMediaProfileConverter
import com.spaceandjonin.mycrd.converters.TimestampConverter
import com.spaceandjonin.mycrd.db.dao.AddedCardDao
import com.spaceandjonin.mycrd.db.dao.LiveCardDao
import com.spaceandjonin.mycrd.models.AddedCard
import com.spaceandjonin.mycrd.models.LiveCard

@Database(entities = [LiveCard::class, AddedCard::class], version = 5, exportSchema = false)

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
package com.spaceandjonin.mycrd.di

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@Module
@InstallIn(FragmentComponent::class)

object FragmentModule{

    @SuppressLint("SimpleDateFormat")
    @Provides
    @FragmentScoped
    @ImageFile
    fun providesImageFile(@ApplicationContext applicationContext: Context): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "MYCRD_JPEG_${timeStamp}",
            ".jpg", storageDir)/*.apply {
            // Save a file: path for use with ACTION_VIEW intents
            //  currentPhotoPath = absolutePath
        }*/
    }
}

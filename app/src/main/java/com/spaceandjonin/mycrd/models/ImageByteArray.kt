package com.spaceandjonin.mycrd.models

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class ImageByteArray @Inject constructor(@ApplicationContext val context: Context) {

    fun getArray(profileUrl: String?): ByteArray? {
        try {
            profileUrl?.let {
                return Glide.with(context).asFile().load(profileUrl).submit().get().readBytes()
            }
        } catch (e: Exception) {
            Timber.d("getArray: ${e.localizedMessage}")
            return null
        }
        return null
    }
}
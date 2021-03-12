package com.codedevtech.mycardv2.models

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageByteArray @Inject constructor(@ApplicationContext val context: Context) {

    fun getArray(profileUrl: String?): ByteArray?{
        try {
            profileUrl?.let {
                return Glide.with(context).asFile().load(profileUrl).submit().get().readBytes()
            }
        }catch (e: Exception){
            Log.d("TAG", "getArray: ${e.localizedMessage}")
            return null
        }
        return null
    }
}
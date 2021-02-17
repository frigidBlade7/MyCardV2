package com.codedevtech.mycardv2.models

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.util.*

@JsonClass(generateAdapter = true)
@Parcelize
data class SocialMediaProfile (var usernameOrUrl: String = "", var type: SocialMedia = SocialMedia.None): Parcelable {

    @get:Exclude
    val id = Calendar.getInstance().timeInMillis

    enum class SocialMedia {
        LinkedIn,Facebook,Twitter,Instagram,None
    }
}
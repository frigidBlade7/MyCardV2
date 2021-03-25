package com.spaceandjonin.mycrd.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.util.*

@JsonClass(generateAdapter = true)
@Parcelize
@Keep
data class SocialMediaProfile (var usernameOrUrl: String = "", var type: SocialMedia = SocialMedia.None): Parcelable {

    @get:Exclude
    val id = Calendar.getInstance().timeInMillis

    @Keep enum class SocialMedia {
        LinkedIn,Facebook,Twitter,Instagram,None
    }
}
package com.spaceandjonin.mycrd.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

@JsonClass(generateAdapter = true)
@Parcelize
@Keep
data class SocialMediaProfile (var usernameOrUrl: String = "", var type: SocialMedia = SocialMedia.LinkedIn): Parcelable {

    @get:Exclude
    @IgnoredOnParcel
    val id = Calendar.getInstance().timeInMillis

    @Keep enum class SocialMedia {
        LinkedIn,Facebook,Twitter,Instagram
    }
}
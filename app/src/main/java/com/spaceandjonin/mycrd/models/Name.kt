package com.spaceandjonin.mycrd.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
@JsonClass(generateAdapter = true)
data class Name(
    var firstName: String = "", var lastName: String = "", var prefix: String? = "",
    var middleName: String = "",
    var fullName: String = "",
    var suffix: String? = ""
) : Parcelable {
}
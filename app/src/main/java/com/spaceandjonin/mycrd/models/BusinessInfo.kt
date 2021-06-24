package com.spaceandjonin.mycrd.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
@JsonClass(generateAdapter = true)
data class BusinessInfo(
    var companyName: String = "",
    var role: String = "",
    var companyAddress: String? = "",
    var website: String? = "",
    var companyLogo: String? = ""
) : Parcelable {

}
package com.spaceandjonin.mycrd.models

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.util.*


@JsonClass(generateAdapter = true)
@Parcelize
data class PhoneNumber ( var number: String = "",var type: PhoneNumberType = PhoneNumberType.Mobile):Parcelable {
    @get:Exclude
    val id = Calendar.getInstance().timeInMillis


    enum class PhoneNumberType {
        Home,Mobile,Work,Other
    }
}
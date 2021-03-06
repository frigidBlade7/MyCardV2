package com.spaceandjonin.mycrd.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class Name(var firstName: String = "", var lastName: String = "",    var prefix: String? ="",
                var middleName: String="",
                var fullName: String="",
                var suffix: String? = ""
):Parcelable {
}
package com.spaceandjonin.mycrd.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class BusinessInfo(var companyName: String = "",
                        var role: String = "",
                        var companyAddress: String? ="",
                        var companyLogo: String?=""): Parcelable {

}
package com.codedevtech.mycardv2.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BusinessInfo(var companyName: String = "",
                        var role: String = "",
                        var companyAddress: String? ="",
                        var companyLogo: String?=""): Parcelable {

}
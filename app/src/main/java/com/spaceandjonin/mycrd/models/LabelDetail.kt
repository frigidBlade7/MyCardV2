package com.spaceandjonin.mycrd.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LabelDetail(val label: String, val detail: String, val parentLabel: String = "") :
    Parcelable
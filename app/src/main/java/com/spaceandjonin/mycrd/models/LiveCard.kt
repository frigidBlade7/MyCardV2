package com.spaceandjonin.mycrd.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Fts4
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Fts4
@Entity
@Parcelize
@Keep
@JsonClass(generateAdapter = true)

data class LiveCard(var owner: String? = FirebaseAuth.getInstance().currentUser?.uid) : Card(),
    Parcelable {

    constructor(baseCard: Card) : this() {
        super.id = baseCard.id
        super.businessInfo = baseCard.businessInfo
        super.createdAt = baseCard.createdAt
        super.emailAddresses = baseCard.emailAddresses
        super.name = baseCard.name
        super.phoneNumbers = baseCard.phoneNumbers
        super.profilePicUrl = baseCard.profilePicUrl
        super.updatedAt = baseCard.updatedAt
        super.socialMediaProfiles = baseCard.socialMediaProfiles
    }

}


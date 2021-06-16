package com.spaceandjonin.mycrd.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Fts4
import kotlinx.parcelize.Parcelize

//todo add ellipses

@Fts4
@Entity
@Parcelize
@Keep
data class AddedCard(var note: String? =""):Card(), Parcelable {

    constructor(baseCard: Card) : this(){
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




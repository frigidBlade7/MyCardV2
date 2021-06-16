package com.spaceandjonin.mycrd.models

import androidx.room.Embedded
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.IgnoredOnParcel

@JsonClass(generateAdapter = true)
open class Card(@DocumentId
                var id: String="",
                var profilePicUrl: String = "",
                var emailAddresses: List<EmailAddress> = listOf(),
                var phoneNumbers: List<PhoneNumber> = listOf(),
                var socialMediaProfiles: List<SocialMediaProfile> = listOf(),
                @Embedded var businessInfo: BusinessInfo = BusinessInfo(),
                @Embedded var name: Name = Name(),
                var createdAt:Timestamp = Timestamp.now(),
                var updatedAt:Timestamp = Timestamp.now()) {

    @get:Exclude
    @IgnoredOnParcel
    var position= 1

}


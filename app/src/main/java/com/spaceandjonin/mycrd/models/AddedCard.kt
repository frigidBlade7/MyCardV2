package com.spaceandjonin.mycrd.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Fts4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

//todo add ellipses

@Fts4
@Entity
@Parcelize
@Keep
data class AddedCard(@DocumentId
                var id: String="",
                     var profilePicUrl: String = "",
                     var emailAddresses: List<EmailAddress> = listOf(),
                     var phoneNumbers: List<PhoneNumber> = listOf(),
                     var socialMediaProfiles: List<SocialMediaProfile> = listOf(),
                     @Embedded var businessInfo: BusinessInfo = BusinessInfo(),
                     var note: String? ="",
                     @Embedded var name: Name = Name(),
                     var createdAt:Timestamp = Timestamp.now(),
                     var updatedAt:Timestamp = Timestamp.now()):Parcelable {


    @get:Exclude
    var position= 1
}




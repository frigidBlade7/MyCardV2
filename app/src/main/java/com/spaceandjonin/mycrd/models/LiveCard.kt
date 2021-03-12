package com.spaceandjonin.mycrd.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Fts4
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Fts4
@Entity
@Parcelize
data class LiveCard(@DocumentId
                var id: String="",

                    var owner: String? = FirebaseAuth.getInstance().currentUser?.uid,
                    var profilePicUrl: String = "",
                    var emailAddresses: List<EmailAddress> = listOf(),
                    var phoneNumbers: List<PhoneNumber> = listOf(),
                    var socialMediaProfiles: List<SocialMediaProfile> = listOf(),
                    @Embedded var businessInfo: BusinessInfo = BusinessInfo(),
                    @Embedded var name: Name = Name(),
                    var createdAt:Timestamp = Timestamp.now(),
                    var updatedAt:Timestamp = Timestamp.now()):Parcelable {

    @get:Exclude
    var position= 1

}


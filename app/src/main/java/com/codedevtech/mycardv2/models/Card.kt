package com.codedevtech.mycardv2.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize
import java.util.*
@Parcelize
data class Card(@DocumentId
                var id: String=""):Parcelable {


    var profilePicUrl: String = ""
    var emailAddresses: List<EmailAddress> = listOf()
    var phoneNumbers: List<PhoneNumber> = listOf()
    var socialMediaProfiles: List<SocialMediaProfile> = listOf()
    var businessInfo: BusinessInfo = BusinessInfo()
    var note: String? =""
    var name: Name = Name()
    var timestamp = Timestamp.now()

    @Exclude
    var position= 1
}

data class PhoneNumber ( var number: String = "") {
    @Exclude
    val id = Calendar.getInstance().timeInMillis
    var type: PhoneNumberType = PhoneNumberType.Mobile

    enum class PhoneNumberType {
        Home,Mobile,Work,Other
    }
}

data class EmailAddress (var address: String = ""){

    @Exclude
    val id = Calendar.getInstance().timeInMillis

    var type: EmailType = EmailType.Personal

    enum class EmailType {
        Personal, Work, Other
    }
}

data class SocialMediaProfile (var usernameOrUrl: String = "", var type: SocialMedia = SocialMedia.None) {

    val id = Calendar.getInstance().timeInMillis

    enum class SocialMedia {
        LinkedIn,Facebook,Twitter,Instagram,None
    }
}

data class BusinessInfo(var companyName: String = "") {
    var role: String = ""
    var companyAddress: String? =""
    var companyLogo: String?=""
}

data class Name(var firstName: String = "", var lastName: String = "") {
    var prefix: String? =""
    var middleName: String=""
    var fullName: String=""
    var suffix: String? = ""

}


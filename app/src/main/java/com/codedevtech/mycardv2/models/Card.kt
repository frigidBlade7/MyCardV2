package com.codedevtech.mycardv2.models

import androidx.lifecycle.MutableLiveData
import java.util.*

data class Card(var id: Int = 0, var name: Name) {


    var imageName: String = ""
    var emailAddresses: List<EmailAddress> = listOf()
    var phoneNumbers: List<PhoneNumber> = listOf()
    var socialMediaProfiles: List<SocialMediaProfile> = listOf()
    var businessInfo: BusinessInfo = BusinessInfo()
    var note: String? =""

}

data class PhoneNumber ( var number: String = "") {
    val id = Calendar.getInstance().timeInMillis
    var type: PhoneNumberType = PhoneNumberType.Mobile

    enum class PhoneNumberType {
        Home,Mobile,Work,Other
    }
}

data class EmailAddress (var address: String = ""){

    val id = Calendar.getInstance().timeInMillis

    var type: EmailType = EmailType.Personal

    enum class EmailType {
        Personal, Work, Other
    }
}

data class SocialMediaProfile (var usernameOrUrl: String = "", var socialMedia: SocialMedia = SocialMedia.None) {

    val id = Calendar.getInstance().timeInMillis

    enum class SocialMedia {
        LinkedIn,Facebook,Twitter,Instagram,None
    }
}

class BusinessInfo {
    var companyName: String = ""
    var role: String = ""
    var companyAddress: String? =""
    var logoUrl: String?=""
}

data class Name(var first: String = "",var last: String = "") {
    var prefix: String? =""
    var middle: String?=""
    var suffix: String? = ""
}

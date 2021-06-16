package com.spaceandjonin.mycrd.utils

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.models.*
import junit.framework.TestCase

class ExtensionsKtTest : TestCase() {

    fun testInitials() {
        val name1 = Name("Nathany")
        assertEquals("N",name1.initials())
        val name2 = Name(lastName = "Attipoe")
        assertEquals("A",name2.initials())

        name1.lastName="Attipoe"
        assertEquals("NA",name1.initials())

        name1.middleName = "Natornam"
        assertEquals("NA", name1.initials())

        name1.prefix="Mr"
        name1.suffix="Jnr"
        assertEquals("NA",name1.initials())

    }

    fun testLiveCardInitials() {
        val name3 = Name("John","Akwasi", "Mr","Doe")
        val liveCard = LiveCard(owner = "12345")
        liveCard.name = name3
        val card = MutableLiveData(liveCard)
        assertEquals("JA",card.value?.initials())
    }

    //todo move to view test
    fun testBackgroundColor() {
        //ImageView()
/*        val myCard = LiveCard()
        val liveCardList = listOf(LiveCard(), LiveCard(),myCard,LiveCard())
        assertEquals(liveCardList.indexOf(myCard)%7)*/
    }

    //todo move to view test
    fun testInitialsColor() {}

    //todo move to view test
    fun testTextColor() {}

    fun testHasAtLeastOne() {
        val socialMediaProfileList = mutableListOf(SocialMediaProfile(),SocialMediaProfile(""))
        assertFalse(socialMediaProfileList.hasAtLeastOne())
        socialMediaProfileList.add(SocialMediaProfile("username@url.com"))
        assertTrue(socialMediaProfileList.hasAtLeastOne())
    }

    fun testHasAll() {
        val socialMediaProfileList = mutableListOf(SocialMediaProfile(),SocialMediaProfile("nate"),
            SocialMediaProfile("kobby")
        )
        assertFalse(socialMediaProfileList.hasAll())
        socialMediaProfileList.removeAt(0)
        assertTrue(socialMediaProfileList.hasAll())
    }

    fun testAggregateNameToFullName() {
        val name = Name("John","Doe", "Mr","Akwasi")
        assertEquals("",name.fullName)
        name.aggregateNameToFullName()
        assertEquals("Mr John Akwasi Doe",name.fullName)

        //test preexisting full name
        val name2 = Name("John","Afranie", "Mr","Dela", "Mr John Akwasi Doe", "Jnr")
        assertEquals("Mr John Akwasi Doe",name.fullName)
        name2.aggregateNameToFullName()
        assertEquals("Mr John Dela Afranie Jnr",name2.fullName)


    }

    fun testGetPrefix() {
        val name = Name(prefix = "Mad")
        assertEquals("", name.getPrefix())
        name.aggregateNameToFullName()
        assertEquals("Mad", name.getPrefix())
    }

    fun testGetSuffix() {
        val name = Name(suffix = "Snr")
        assertEquals("",name.getSuffix())
        name.aggregateNameToFullName()
        assertEquals("Snr",name.getSuffix())
    }

    fun testGetNameOnly() {
        val name = Name("John","Afranie",  "Mr", "Dela", suffix= "Jnr")
        name.aggregateNameToFullName()
        assertEquals(listOf("John", "Dela", "Afranie"), name.getNameOnly())

    }

    fun testSegregateFullName() {
        val name = Name(fullName = "Mr John Dela Afranie Jnr")
        name.segregateFullName()
        assertEquals("Mr",name.prefix)
        assertEquals("John",name.firstName)
        assertEquals("Dela",name.middleName)
        assertEquals("Afranie",name.lastName)
        assertEquals("Jnr",name.suffix)
    }

    fun testIsNotEmpty() {
        val name = Name(prefix = "Mr", suffix = "Snr")
        assertFalse(name.isNotEmpty())
        name.firstName = "Nate"
        assertTrue(name.isNotEmpty())
        name.firstName = ""
        assertFalse(name.isNotEmpty())
        name.fullName = "Nate Att"
        assertTrue(name.isNotEmpty())
    }

    //todo move to view test?
    fun testNotifyObserver() {
        //todo research livedata testing
    }

    fun testFullname() {
        val name = Name("John","Doe", "Mr", suffix = "Jnr")
        assertEquals("Mr John Doe Jnr",name.fullname())
        name.middleName = "Akwasi Constantine"
        assertEquals("Mr John Akwasi Constantine Doe Jnr",name.fullname())

    }

    fun testGetCode() {
        //todo confirm if this is the right way  to create the exception class going forward

        //assertEquals(R.string.invalid_credentials, FirebaseAuthInvalidCredentialsException("error","error").getCode())
        //assertEquals(R.string.too_many_tries, FirebaseTooManyRequestsException("error").getCode())
        //assertEquals(R.string.invalid_credentials, Exception("error").getCode())
    }

    //todo move to view test
    fun testHideKeyboard() {}

    //todo move to view test
    fun testShowKeyboard() {}

    //todo move to view test
    fun testShow() {}
    /*
    fun testExportVCard() {
        val card = Card("1","",
        listOf(EmailAddress("email@gmail.com",EmailAddress.EmailType.PersoKnal)),
        listOf(PhoneNumber("243875",PhoneNumber.PhoneNumberType.Other)),
            listOf(SocialMediaProfile("url",SocialMediaProfile.SocialMedia.LinkedIn)),
        BusinessInfo("nateIndustries","nateindustries.com"),
        Name("Jon","Att","Mr","Kwesi",suffix = "Jnr"))
            todo Caused by: java.lang.RuntimeException: Method getMainLooper in android.os.Looper not mocked
        val liveCard = LiveCard(card)
        liveCard.owner = "3"
        liveCard.name.aggregateNameToFullName()

        val vcard = liveCard.exportVCard(null)
        //check name
        assertEquals(liveCard.name.lastName,vcard.structuredName.family)
        //role
        assertEquals(liveCard.businessInfo.role,vcard.roles[0].value)
        //email
        assertEquals(liveCard.emailAddresses[0].address,vcard.emails[0].value)

        //and phone num
        assertEquals(liveCard.phoneNumbers[0].number,vcard.telephoneNumbers[0].text)

    }*/
    fun testExportContactIntent() {}
       /*todo robolectric val addedCard = AddedCard("1","2",
            listOf(EmailAddress("email@gmail.com",EmailAddress.EmailType.Personal)),
            listOf(PhoneNumber("243875",PhoneNumber.PhoneNumberType.Other)),
            listOf(SocialMediaProfile("url",SocialMediaProfile.SocialMedia.LinkedIn)),
            BusinessInfo("nateIndustries","nateindustries.com"),"",
            Name("Jon","Att","Mr","Kwesi",suffix = "Jnr"))

        val intent = addedCard.exportContactIntent(null)
        assertEquals(Intent.ACTION_INSERT_OR_EDIT,intent.action)
        assertEquals(true,intent.getBooleanExtra("finishActivityOnSaveCompleted", false))
*/

}
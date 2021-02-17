package com.codedevtech.mycardv2.models

import com.google.firebase.firestore.DocumentId

data class User(@DocumentId val uid: String, var phoneNumber: String?="", var name: String?=""){
    //var profileUrl: String =""
}
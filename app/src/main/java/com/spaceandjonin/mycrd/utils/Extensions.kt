package com.spaceandjonin.mycrd.utils

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.util.Util
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.models.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.ByteArrayOutputStream


fun Name?.initials():String{
    this?.let {
        return "${firstName.trim().getOrNull(0)?:""}${lastName.trim().getOrNull(0)?:""}".trim()
    }
    return ""
}

fun LiveCard?.initials():String{
    this?.let {
        return "${name.firstName.trim().getOrNull(0)?:""}${name.lastName.trim().getOrNull(0)?:""}".trim()
    }
    return ""
}

fun View.backgroundColor(colorCode: Int): Int{
    return when(colorCode%7){
        0 -> ContextCompat.getColor(context, R.color.mc_purple_10)
        1 -> ContextCompat.getColor(context, R.color.mc_blue_20)
        2-> ContextCompat.getColor(context, R.color.mc_orange_30)
        3 -> ContextCompat.getColor(context, R.color.mc_brown_30)
        4 -> ContextCompat.getColor(context, R.color.mc_teal_30)
        5-> ContextCompat.getColor(context, R.color.mc_wine_30)
        6-> ContextCompat.getColor(context, R.color.mc_navy_30)
        else -> ContextCompat.getColor(context, R.color.mc_gray_light)
    }
}

fun View.initialsColor(colorCode: Int): Int{
    return when(colorCode%7){
        0 -> ContextCompat.getColor(context, R.color.mc_purple)
        1 -> ContextCompat.getColor(context, R.color.mc_blue)
        2-> ContextCompat.getColor(context, R.color.mc_orange)
        3 -> ContextCompat.getColor(context, R.color.mc_brown)
        4 -> ContextCompat.getColor(context, R.color.mc_teal)
        5-> ContextCompat.getColor(context, R.color.mc_wine)
        6-> ContextCompat.getColor(context, R.color.mc_navy)
        else -> ContextCompat.getColor(context, android.R.color.darker_gray)
    }
}

fun View.textColor(type: String?): Int{
    return when(type){
        this.context.getString(R.string.personal) -> ContextCompat.getColor(context, R.color.mc_purple)
        this.context.getString(R.string.mobile) -> ContextCompat.getColor(context, R.color.mc_purple)
        this.context.getString(R.string.home) -> ContextCompat.getColor(context, R.color.mc_purple)
        this.context.getString(R.string.work) -> ContextCompat.getColor(context, R.color.mc_green)
        else -> ContextCompat.getColor(context, R.color.mc_orange)
    }
}

fun List<SocialMediaProfile>.hasAtLeastOne(): Boolean{
    return this.any { it.usernameOrUrl.trim().isNotEmpty() }
}
fun List<SocialMediaProfile>.hasAll(): Boolean{
    return this.all { it.usernameOrUrl.trim().isNotEmpty() }
}

fun Name.aggregateNameToFullName(){
    fullName = "$prefix $firstName $middleName $lastName $suffix".trim().replace("\\s+".toRegex(), " ")

}

fun Name.getPrefix(): String{
    val components = fullName.split(" ").toTypedArray()
    return components.filter { Utils.PREFIXES.contains(it.toLowerCase())}.joinToString(" ")
}

fun Name.getSuffix(): String{
    val components = fullName.split(" ").toTypedArray()
    return components.filter { Utils.SUFFIXES.contains(it.toLowerCase())}.joinToString(" ")
}

fun Name.getNameOnly(): List<String>{
    val components = fullName.split(" ").toTypedArray()
    return components.filterNot { Utils.SUFFIXES.contains(it.toLowerCase()) || Utils.PREFIXES.contains(it.toLowerCase())}
}

fun Name.segregateFullName(){
    val list =  getNameOnly()

    prefix = getPrefix()
    suffix = getSuffix()

    Log.d("TAG", "segregateFullName: ${list.size}")
    when (list.size){
        1-> firstName = list.getOrNull(0)?:""
        2-> {
            firstName = list.getOrNull(0)?:""
            if(middleName.trim().isEmpty()) {
                lastName = list.getOrNull(1) ?: ""
                middleName =""
            }
            else {
                middleName = list.getOrNull(1) ?: ""
                lastName =""
            }
        }
        3->{

            firstName = list.getOrNull(0)?:""
            middleName = list.getOrNull(1) ?:""
            lastName = list.getOrNull(2) ?:""
        }
        else->{
            firstName = list.getOrNull(0)?:""
            lastName = list.getOrNull(list.size-1)?:""
            middleName = list.drop(1).dropLast(1).joinToString (separator = " ")
        }
    }
}

fun Name.isNotEmpty(): Boolean {
    return fullName.trim().isNotEmpty() || firstName.trim().isNotEmpty() || lastName.trim().isNotEmpty() || middleName.trim().isNotEmpty()
}

//just gonna use this to trigger the livedata since it behaves a little fuzzy with lists
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}


fun <T : Parcelable> deepClone(objectToClone: T): T? {
    var parcel: Parcel? = null
    return try {
        parcel = Parcel.obtain()
        parcel.writeParcelable(objectToClone, 0)
        parcel.setDataPosition(0)
        parcel.readParcelable(objectToClone::class.java.classLoader)
    } finally {
        //it is important to recyle parcel and free up resources once done.
        parcel?.recycle()
    }
}

fun Name?.fullname():String{
    this?.let {
        if(it.middleName.isNotEmpty())
            return "${it.prefix} ${it.firstName} ${it.middleName} ${it.lastName} ${it.suffix}".trim()
        return "${it.prefix} ${it.firstName} ${it.lastName} ${it.suffix}".trim()
    }
    return ""
}



fun Exception.getCode(): Int{
    return when(this){
        is FirebaseAuthInvalidCredentialsException -> {
            R.string.invalid_credentials
        }
        is FirebaseTooManyRequestsException -> {
            R.string.too_many_tries
        }
        else ->{
            Log.d("ERROR", "onVerificationFailed: ${this.localizedMessage}")
            R.string.default_error
        }
    }
}

fun BottomNavigationView.hide() {
    if (visibility == View.GONE) return

    val drawable = BitmapDrawable(context.resources, drawToBitmap())
    val parent = parent as ViewGroup
    drawable.setBounds(left, top, right, bottom)
    parent.overlay.add(drawable)
    visibility = View.GONE
    ValueAnimator.ofInt(top, parent.height).apply {
        startDelay = 100L
        duration = 200L
        interpolator = AnimationUtils.loadInterpolator(
            context,
            android.R.interpolator.fast_out_linear_in
        )
        addUpdateListener {
            val newTop = it.animatedValue as Int
            drawable.setBounds(left, newTop, right, newTop + height)
        }
        doOnEnd {
            parent.overlay.remove(drawable)
        }
        start()
    }
}

fun Activity.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
fun Activity.showKeyboard(/*view: View*/) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun BottomNavigationView.show() {
    if (visibility == View.VISIBLE) return

    val parent = parent as ViewGroup
    // View needs to be laid out to create a snapshot & know position to animate. If view isn't
    // laid out yet, need to do this manually.
    if (!isLaidOut) {
        measure(
            View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.AT_MOST)
        )
        layout(parent.left, parent.height - measuredHeight, parent.right, parent.height)
    }

    val drawable = BitmapDrawable(context.resources, drawToBitmap())
    drawable.setBounds(left, parent.height, right, parent.height + height)
    parent.overlay.add(drawable)
    ValueAnimator.ofInt(parent.height, top).apply {
        startDelay = 100L
        duration = 300L
        interpolator = AnimationUtils.loadInterpolator(
            context,
            android.R.interpolator.linear_out_slow_in
        )
        addUpdateListener {
            val newTop = it.animatedValue as Int
            drawable.setBounds(left, newTop, right, newTop + height)
        }
        doOnEnd {
            parent.overlay.remove(drawable)
            visibility = View.VISIBLE
        }
        start()
    }
}

fun Bitmap.toByteArray(): ByteArray {

    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos )
    return baos.toByteArray()
}
@OptIn(ExperimentalCoroutinesApi::class)
fun Query.awaitContinuous(): Flow<QuerySnapshot?> = callbackFlow {

    val subscriptionCallback = addSnapshotListener { value, error ->
        if (error!=null) {
            cancel("error fetching live update of collection at path", error.cause)
            return@addSnapshotListener
        }
        offer(value)
    }

    awaitClose { subscriptionCallback.remove() }
}

/*fun CountryCodePicker.customizeDialog(context: Context){
    this.setDialogEventsListener(object : CountryCodePicker.DialogEventsListener {
        override fun onCcpDialogDismiss(dialogInterface: DialogInterface?) {
            dialogInterface?.cancel()
        }
        override fun onCcpDialogCancel(dialogInterface: DialogInterface?) {
            dialogInterface?.cancel()
        }
        override fun onCcpDialogOpen(dialog: Dialog?) {
            dialog?
            val window = dialog?.window
            //window?.setContentView(R.layout.layout_picker_dialog)
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT)

        }
    })
}*/
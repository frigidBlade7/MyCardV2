package com.spaceandjonin.mycrd.utils

import android.Manifest

class Utils {

    companion object {
        const val SORT_MODE_NAME= "NAME"
        const val SORT_MODE_RECENT= "RECENT"
        const val PATH_ID= "pathId"
        const val PHOTO_URI= "photoUri"


        const val FILTER_ALL= "ALL"
        const val FILTER_COMPANY= "COMPANY"
        const val FILTER_ROLE= "ROLE"
        const val FILTER_NAME= "NAME"
        const val REQUEST_PHOTO: Int = 77
        const val REQUEST_CAMERA: Int = 79
        const val REQUEST_IMAGE_GET: Int = 65
        const val REQUEST_IMAGE_CAPTURE: Int = 68
        const val PAGE_SIZE: Long = 3
        const val RC_CAMERA = 59
        const val IMAGE_FILE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val PHOTO_EXTENSION = ".jpg"
        const val STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
        const val CAMERA_PERMISSION = Manifest.permission.CAMERA

        /** Milliseconds used for UI animations */
        const val ANIMATION_FAST_MILLIS = 50L
        const val ANIMATION_SLOW_MILLIS = 100L
        const val IMAGE_CAPTURE_TIMEOUT_MILLIS: Long = 5000

        const val TIMEOUT: Long = 60000

        val PREFIXES = listOf("mad","mr", "ms", "mrs", "dr", "adm", "capt", "chief", "cmdr", "col", "gov", "hon", "maj", "msgt", "prof", "rev")

        val SUFFIXES = listOf("phd",
            "ccna",
            "obe",
            "sr",
            "jr",
            "i",
            "ii",
            "iii",
            "iv",
            "v",
            "vi",
            "vii",
            "viii",
            "ix",
            "x",
            "snr",
            "jnr")

    }
}
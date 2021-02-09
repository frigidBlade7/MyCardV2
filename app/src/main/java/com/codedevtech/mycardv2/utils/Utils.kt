package com.codedevtech.mycardv2.utils

import android.Manifest

class Utils {

    companion object {
        const val REQUEST_PHOTO: Int = 77
        const val PAGE_SIZE: Long = 10
        const val RC_CAMERA = 59
        const val IMAGE_FILE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val PHOTO_EXTENSION = ".jpg"
        const val STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE

        /** Milliseconds used for UI animations */
        const val ANIMATION_FAST_MILLIS = 50L
        const val ANIMATION_SLOW_MILLIS = 100L
        const val IMAGE_CAPTURE_TIMEOUT_MILLIS: Long = 5000

        const val TIMEOUT: Long = 60000

    }
}
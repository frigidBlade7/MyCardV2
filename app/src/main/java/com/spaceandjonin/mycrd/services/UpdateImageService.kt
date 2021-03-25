package com.spaceandjonin.mycrd.services

import android.net.Uri
import com.spaceandjonin.mycrd.models.Resource

interface UpdateImageService {
    suspend fun uploadImage(pathId: String): Resource<Uri>

    fun setUri(uri: Uri)
}
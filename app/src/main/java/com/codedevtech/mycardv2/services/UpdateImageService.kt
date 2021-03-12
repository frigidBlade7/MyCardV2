package com.codedevtech.mycardv2.services

import android.net.Uri
import com.codedevtech.mycardv2.models.Resource

interface UpdateImageService {
    suspend fun uploadImage(pathId: String): Resource<Uri>

    fun setUri(uri: Uri)
}
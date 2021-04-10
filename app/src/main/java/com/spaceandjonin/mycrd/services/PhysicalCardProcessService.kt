package com.spaceandjonin.mycrd.services

import android.graphics.Bitmap
import com.spaceandjonin.mycrd.models.Resource

interface PhysicalCardProcessService<T> {

    suspend fun processPhysicalCardImage(bitmap: Bitmap?):Resource<T>

}
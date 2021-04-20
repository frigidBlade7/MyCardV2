package com.spaceandjonin.mycrd.services

import android.graphics.Bitmap
import com.google.mlkit.vision.text.Text
import com.spaceandjonin.mycrd.models.Resource

interface PhysicalCardProcessService<T> {

    suspend fun processPhysicalCardImage(bitmap: Bitmap?): Resource<List<Text.Element>?>

}
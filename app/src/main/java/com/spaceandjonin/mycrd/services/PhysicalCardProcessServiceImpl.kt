package com.spaceandjonin.mycrd.services

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.models.LiveCard
import com.spaceandjonin.mycrd.models.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PhysicalCardProcessServiceImpl @Inject constructor(val textRecognizer: TextRecognizer) : PhysicalCardProcessService<LiveCard?> {

    override suspend fun processPhysicalCardImage(bitmap: Bitmap?): Resource<LiveCard?> {
        bitmap?.let {

            val image = InputImage.fromBitmap(it, 0)
            return try {

                processText(textRecognizer.process(image).await())

            }catch (e: Exception){
                e.printStackTrace()
                Resource.Error(R.string.could_not_process_image)
            }
        }
        return Resource.Error(R.string.could_not_process_image)
    }


    fun processText(text: Text):Resource<LiveCard?>{
        val blocks = text.textBlocks
        if (blocks.size==0)
            return Resource.Success(LiveCard())

        for (block in blocks) {
            val lines = block.lines
            for (line in lines){
                val elements = line.elements
                for (element in elements) {
                    Log.d(Companion.TAG, "processedText: ${element.text}")
                    return Resource.Success(LiveCard())
                }
            }
        }
        return Resource.Error(R.string.could_not_process_text)
    }

    companion object {
        private const val TAG = "PhysicalCardProcessServ"
    }
}
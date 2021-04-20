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

    override suspend fun processPhysicalCardImage(bitmap: Bitmap?): Resource<List<Text.Element>?> {
        bitmap?.let {

            val image = InputImage.fromBitmap(it, 0)
            return try {

                return processText(textRecognizer.process(image).await())
                //return Resource.Success(LiveCard())

            }catch (e: Exception){
                e.printStackTrace()
                Resource.Error(R.string.could_not_process_image)
            }
        }
        return Resource.Error(R.string.could_not_process_image)
    }


    fun processText(text: Text):Resource<List<Text.Element>?>{
        Log.d(TAG, "processText: hi")

        val allElements = mutableListOf<Text.Element>()
        val blocks = text.textBlocks
        if (blocks.size==0) {
            Log.d(TAG, "processText: natin")
        }

        for (block in blocks) {
            Log.d(TAG, "processText: ${block.text}")
            val lines = block.lines
            for (line in lines){
                val elements = line.elements
                for (element in elements) {
                    allElements.add(element)
                    Log.d(TAG, "processedText: ${element.text}")
                }
            }
            //return Resource.Success(LiveCard())
        }

        return Resource.Success(allElements)
    }

    companion object {
        private const val TAG = "PhysicalCardProcessServ"
    }
}
package com.spaceandjonin.mycrd.services

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.models.LiveCard
import com.spaceandjonin.mycrd.models.Resource
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class PhysicalCardProcessServiceImpl @Inject constructor(val textRecognizer: TextRecognizer) :
    PhysicalCardProcessService<LiveCard?> {

    override suspend fun processPhysicalCardImage(bitmap: Bitmap?): Resource<List<Text.Line>?> {
        bitmap?.let {

            val image = InputImage.fromBitmap(it, 0)
            return try {
                return processText(textRecognizer.process(image).await())
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(R.string.could_not_process_image)
            }
        }
        return Resource.Error(R.string.could_not_process_image)
    }


    fun processText(text: Text): Resource<List<Text.Line>?> {
        Timber.d( "processText: hi")

        val allElements = mutableListOf<Text.Line>()
        val blocks = text.textBlocks
        if (blocks.size == 0) {
            Timber.d( "processText: natin")
        }

        for (block in blocks) {
            Timber.d( "processText: ${block.text}")
            val lines = block.lines
            for (line in lines)
                allElements.add(line)
        }

        return Resource.Success(allElements)
    }

    companion object {
        private const val TAG = "PhysicalCardProcessServ"
    }
}
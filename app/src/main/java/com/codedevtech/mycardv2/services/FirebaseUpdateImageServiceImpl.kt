package com.codedevtech.mycardv2.services

import android.net.Uri
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.models.Resource
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FirebaseUpdateImageServiceImpl @Inject constructor(val storage: FirebaseStorage): UpdateImageService {

    val reference = storage.reference.child("images")
    var photoUri: Uri? = null
    var byteData: ByteArray? = null
    lateinit var dataTask: UploadTask


    override suspend fun uploadImage(pathId: String): Resource<Uri> {
        val resource = reference.child(pathId)
        return try {

            photoUri?.let {
                dataTask = resource.putFile(it)
            }

            byteData?.let {
                dataTask = resource.putBytes(it)
            }

            val uri = dataTask.continueWithTask { task ->
                if (!task.isSuccessful)
                    throw task.exception!!
                resource.downloadUrl
            }.await()

            Resource.Success(uri)


        }catch (e: Exception){
            Resource.Error(R.string.failed)
        }
    }

    override fun setUri(uri: Uri) {
        photoUri = uri
    }
}
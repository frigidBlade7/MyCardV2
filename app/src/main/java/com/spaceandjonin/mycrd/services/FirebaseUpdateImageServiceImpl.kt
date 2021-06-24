package com.spaceandjonin.mycrd.services

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.utils.getCode
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "FirebaseUpdateImageServ"


class FirebaseUpdateImageServiceImpl @Inject constructor(val storage: FirebaseStorage) :
    UpdateImageService {


    val reference = storage.reference.child("images")
    var photoUri: Uri? = null
    var sessionUri: Uri? = null
    lateinit var dataTask: UploadTask


    override suspend fun uploadImage(pathId: String): Resource<Uri> {
        val resource = reference.child(pathId)
        return try {

            photoUri?.let {
                dataTask = resource.putFile(it)
                sessionUri = dataTask.snapshot.uploadSessionUri
            }

            val uri = dataTask.continueWithTask { task ->
                if (!task.isSuccessful)
                    throw task.exception!!
                resource.downloadUrl
            }.await()

            Resource.Success(uri)


        } catch (e: Exception) {
            Timber.d( "uploadImage: ${e.localizedMessage}")
            Timber.d( "uploadImage: ${e.getCode()}")
            Resource.Error(R.string.failed)
        }
    }

    override fun setUri(uri: Uri) {
        photoUri = uri
    }

}
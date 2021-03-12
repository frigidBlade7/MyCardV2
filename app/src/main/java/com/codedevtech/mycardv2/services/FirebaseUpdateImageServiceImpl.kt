package com.codedevtech.mycardv2.services

import android.net.Uri
import android.util.Log
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.utils.Utils
import com.codedevtech.mycardv2.utils.getCode
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "FirebaseUpdateImageServ"


class FirebaseUpdateImageServiceImpl @Inject constructor(val storage: FirebaseStorage): UpdateImageService {


    val reference = storage.reference.child("images")
    var photoUri: Uri? = null
    var sessionUri: Uri?= null
    //var byteData: ByteArray? = null
    lateinit var dataTask: UploadTask


    override suspend fun uploadImage(pathId: String): Resource<Uri> {
        val resource = reference.child(pathId)
        return try {

            photoUri?.let {
                dataTask = resource.putFile(it)
                sessionUri = dataTask.snapshot.uploadSessionUri
            }

/*            byteData?.let {
                dataTask = resource.putBytes(it)
            }*/

            val uri = dataTask.continueWithTask { task ->
                if (!task.isSuccessful)
                    throw task.exception!!
                resource.downloadUrl
            }.await()

            Resource.Success(uri)


        }catch (e: Exception){
            Log.d(TAG, "uploadImage: ${e.localizedMessage}")
            Log.d(TAG, "uploadImage: ${e.getCode()}")
            //todo save session url
            Resource.Error(R.string.failed)
        }
    }

    override fun setUri(uri: Uri) {
        photoUri = uri
    }

}
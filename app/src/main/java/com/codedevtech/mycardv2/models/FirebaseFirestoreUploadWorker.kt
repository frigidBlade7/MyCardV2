package com.codedevtech.mycardv2.models

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.util.Util
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.utils.Utils

import com.codedevtech.mycardv2.utils.getCode
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFirestoreUploadWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {



    lateinit var dataTask: UploadTask


    override fun doWork(): Result {

        // Do the work here--in this case, upload the images.

        uploadImage(inputData.getString(Utils.PATH_ID)?: return Result.failure(),
            inputData.getString(Utils.PHOTO_URI)?: return Result.failure())
        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }


      fun uploadImage(pathId: String, photoUri: String)/*: Resource<Uri> */{
        val resource = FirebaseStorage.getInstance().reference.child("images")
            .child(pathId)
        /*return*/ try {

            Uri.parse(photoUri)?.let {
                dataTask = resource.putFile(it)
            }

            val uri = dataTask.continueWithTask { task ->
                if (!task.isSuccessful)
                    throw task.exception!!
                resource.downloadUrl
            }//.await()

              //addedCardsRepository.firebaseAddedCardDataSource.updateCardProfilePhoto
            Resource.Success(uri)


        }catch (e: Exception){
            Log.d("TAG", "uploadImage: ${e.localizedMessage}")
            Log.d("TAG", "uploadImage: ${e.getCode()}")
            Resource.Error(R.string.failed)
        }
    }
}

package com.spaceandjonin.mycrd.models

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.models.datasource.FirebaseLiveCardDataSourceImpl
import com.spaceandjonin.mycrd.utils.Utils

import com.spaceandjonin.mycrd.utils.getCode
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

//@HiltWorker
class FirebaseFirestoreUploadWorkerLiveCards @AssistedInject constructor(/*@Assisted*/ appContext: Context,
                                                                /*@Assisted */workerParams: WorkerParameters
):
    CoroutineWorker(appContext, workerParams) {

    lateinit var dataTask: UploadTask


    override suspend fun doWork(): Result {

        // Do the work here--in this case, upload the images.

        uploadImage(inputData.getString(Utils.PATH_ID)?: return Result.failure(),
            inputData.getString(Utils.PHOTO_URI)?: return Result.failure())
        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }


      private suspend fun uploadImage(cardId: String, photoUri: String): Resource<Uri> {

        return try {
            val resource = FirebaseStorage.getInstance().reference.child("images")
                .child("images/profiles/$cardId")

            Uri.parse(photoUri)?.let {
                dataTask = resource.putFile(it)
            }

            val uri = dataTask.continueWithTask { task ->
                if (!task.isSuccessful)
                    throw task.exception!!
                resource.downloadUrl

            }.await()

            FirebaseLiveCardDataSourceImpl(FirebaseFirestore.getInstance().collection("personalCards"))
                .updateCardProfilePhoto(uri.toString(),cardId)

            Resource.Success(uri)


        }catch (e: Exception){
            Log.d("TAG", "uploadImage: ${e.localizedMessage}")
            Log.d("TAG", "uploadImage: ${e.getCode()}")
            Resource.Error(R.string.failed)
        }
    }
}

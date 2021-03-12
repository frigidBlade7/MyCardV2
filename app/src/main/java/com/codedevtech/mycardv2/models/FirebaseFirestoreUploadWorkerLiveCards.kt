package com.codedevtech.mycardv2.models

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.util.Util
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.db.dao.AddedCardDao
import com.codedevtech.mycardv2.models.datasource.AddedCardDataSource
import com.codedevtech.mycardv2.models.datasource.FirebaseAddedCardDataSourceImpl
import com.codedevtech.mycardv2.models.datasource.FirebaseLiveCardDataSourceImpl
import com.codedevtech.mycardv2.repositories.AddedCardsRepository
import com.codedevtech.mycardv2.utils.Utils

import com.codedevtech.mycardv2.utils.getCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

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

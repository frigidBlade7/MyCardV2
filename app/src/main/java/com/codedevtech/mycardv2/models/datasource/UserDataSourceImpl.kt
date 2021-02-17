package com.codedevtech.mycardv2.models.datasource

import android.net.Uri
import android.util.Log
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.models.User
import com.codedevtech.mycardv2.services.FirebaseUpdateImageServiceImpl
import com.codedevtech.mycardv2.services.UpdateImageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "UserDataSourceImpl"

abstract class UserDataSourceImpl: DataSource<User> {

    abstract var auth: FirebaseAuth

    //todo change to use firebase user object creator
    override suspend fun addData(data: User): Resource<String> {
        return try {
            val profile = userProfileChangeRequest {
                displayName = data.name
                //photoUri = Uri.parse(data.profileUrl)
            }

            val updateData = auth.currentUser?.updateProfile(profile)?.await()
            Resource.Success(data.uid)
        }catch (e: Exception){
            Log.d(TAG, "updateUserRecord: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    suspend fun updateImage(data: Uri): Resource<String> {
        return try {
            val profile = userProfileChangeRequest {
                photoUri = data
            }

            val updateData = auth.currentUser?.updateProfile(profile)?.await()
            Resource.Success(auth.currentUser?.uid!!)
        }catch (e: Exception){
            Log.d(TAG, "updateUserRecord: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    override suspend fun removeData(data: User): Resource<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateData(data: User): Resource<String> {
        return try {
            val profile = userProfileChangeRequest {
                displayName = data.name
                //photoUri = Uri.parse(data.profileUrl)

            }

            val updateData = auth.currentUser?.updateProfile(profile)?.await()
            Resource.Success(data.uid)
        }catch (e: Exception){
            Log.d(TAG, "updateUserRecord: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    override fun getData(id: String): Flow<Resource<User>> {
        TODO()
/*        auth.currentUser?.let {
            val user = User(it.uid, it.phoneNumber, it.displayName)
            return Resource.Success(user)
        }*/
    }

}
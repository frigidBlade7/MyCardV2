package com.codedevtech.mycardv2.models.datasource

import android.net.Uri
import android.util.Log
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.models.PhoneNumber
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.models.User
import com.codedevtech.mycardv2.services.FirebaseUpdateImageServiceImpl
import com.codedevtech.mycardv2.services.UpdateImageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "UserDataSourceImpl"

abstract class UserDataSourceImpl: DataSource<User> {

    abstract var auth: FirebaseAuth

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
            Resource.Success(data.toString())
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
            Resource.Success(data.name!!)
        }catch (e: Exception){
            Log.d(TAG, "updateUserRecord: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    suspend fun updatePhoneNumber(phoneNumber: String): Resource<String> {
        return try {
            val profile = userProfileChangeRequest {
                //displayName = data.name
                //photoUri = Uri.parse(data.profileUrl)

            }

            val updateData = auth.currentUser?.updateProfile(profile)?.await()
            Resource.Success("data.name!!")
        }catch (e: Exception){
            Log.d(TAG, "updateUserRecord: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    override fun getData(id: String): Flow<Resource<User>> {

        return flow {
            try {
                auth.currentUser?.let {
                    val user = User(
                        it.uid,
                        it.phoneNumber,
                        it.displayName,
                        it.photoUrl.toString()
                    )
                    emit(Resource.Success(user))
                }
            }catch (e: Exception){
                emit(Resource.Error(R.string.default_error))
            }
        }

    }

}
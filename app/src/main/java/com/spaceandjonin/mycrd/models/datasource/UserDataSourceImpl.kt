package com.spaceandjonin.mycrd.models.datasource

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

private const val TAG = "UserDataSourceImpl"

abstract class UserDataSourceImpl : DataSource<User> {

    abstract var auth: FirebaseAuth

    override suspend fun addData(data: User): Resource<String?> {
        return try {
            val profile = userProfileChangeRequest {
                displayName = data.name
                //photoUri = Uri.parse(data.profileUrl)
            }

            auth.currentUser?.updateProfile(profile)?.await()
            Resource.Success(data.uid)
        } catch (e: Exception) {
            Timber.d( "updateUserRecord: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    suspend fun updateImage(data: Uri): Resource<String> {
        return try {
            val profile = userProfileChangeRequest {
                photoUri = data
            }

            auth.currentUser?.updateProfile(profile)?.await()
            Resource.Success(data.toString())
        } catch (e: Exception) {
            Timber.d( "updateUserRecord: ${e.localizedMessage}")
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
        } catch (e: Exception) {
            Timber.d( "updateUserRecord: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    suspend fun updatePhoneNumber(phoneNumber: String): Resource<String> {
        return try {
            val profile = userProfileChangeRequest {
                //displayName = data.name
                //photoUri = Uri.parse(data.profileUrl)

            }

            auth.currentUser?.updateProfile(profile)?.await()
            Resource.Success("data.name!!")
        } catch (e: Exception) {
            Timber.d( "updateUserRecord: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    override fun getData(id: String?): Flow<Resource<User>> {

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
            } catch (e: Exception) {
                emit(Resource.Error(R.string.default_error))
            }
        }

    }

}
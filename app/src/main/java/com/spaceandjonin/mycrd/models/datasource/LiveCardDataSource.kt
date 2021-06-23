package com.spaceandjonin.mycrd.models.datasource

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.models.LiveCard
import com.spaceandjonin.mycrd.models.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

abstract class LiveCardDataSource : DataSource<LiveCard> {

    abstract var collectionReference: CollectionReference


    override suspend fun addData(data: LiveCard): Resource<String?> {
        return try {
            val result = collectionReference.document()
            result.set(data)
            Resource.Success(result.id)
        } catch (e: Exception) {
            Timber.d( "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    override suspend fun removeData(data: LiveCard): Resource<String> {
        return try {
            collectionReference.document(data.id).delete()//.await()
            Resource.Success(data.id)
        } catch (e: Exception) {
            Timber.d( "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    override suspend fun updateData(data: LiveCard): Resource<String> {
        return try {
            Timber.d( "updateData: ${data.id}")
            data.updatedAt = Timestamp.now()
            collectionReference.document(data.id).set(data)//.await()
            Resource.Success(data.id)
        } catch (e: Exception) {
            Timber.d( "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    fun updateCardProfilePhoto(url: String, cardId: String): Resource<Int> {
        return try {
            collectionReference.document(cardId).update("profilePicUrl", url)//.await()
            Resource.Success(R.string.success)
        } catch (e: Exception) {
            Timber.d( "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    suspend fun updateCardCompanyLogo(url: String, cardId: String): Resource<Int> {
        return try {
            collectionReference.document(cardId).update(mapOf("businessInfo.companyLogo" to url))
                .await()
            Resource.Success(R.string.success)
        } catch (e: Exception) {
            Timber.d( "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    override fun getData(id: String?): Flow<Resource<LiveCard>> = callbackFlow {
        id?.let { id ->
            trySend(Resource.Loading)

            val subscriptionCallback =
                collectionReference.document(id).addSnapshotListener { value, error ->
                    value?.let {
                        try {
                            if (it.exists())
                                trySend(Resource.Success(it.toObject(LiveCard::class.java)!!))
                            else
                                trySend(Resource.Error(R.string.card_not_found))
                        } catch (e: Exception) {
                            Timber.d( "getData: ${e.localizedMessage}")
                            trySend(Resource.Error(R.string.card_not_found))

                        }
                    }
                }

            awaitClose { subscriptionCallback.remove() }
        }

    }

    fun getList(owner: String): Flow<Resource<List<LiveCard>>> = callbackFlow {

        trySend(Resource.Loading)
        val subscriptionCallback = collectionReference.whereEqualTo("owner", owner)
            .orderBy("createdAt", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            value?.let {
                //todo is this necessary if(!it.isEmpty)
                try {
                    //todo save to room

                    trySend(Resource.Success(value.toObjects(LiveCard::class.java)))
                } catch (e: Exception) {
                    Timber.d( "getData: ${e.localizedMessage}")
                    trySend(Resource.Error(R.string.cards_not_found))

                }
            }
        }

        awaitClose { subscriptionCallback.remove() }
    }
    companion object {
        private const val TAG = "CardDataSource"
    }


}
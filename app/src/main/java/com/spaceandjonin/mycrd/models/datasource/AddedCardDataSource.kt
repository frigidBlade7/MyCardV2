package com.spaceandjonin.mycrd.models.datasource

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.models.AddedCard
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.utils.getCode
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

abstract class AddedCardDataSource : DataSource<AddedCard> {

    abstract var collectionReference: CollectionReference
    //todo review1 abstract var cm: ConnectivityManager


    override suspend fun addData(data: AddedCard): Resource<String?> {
        return try {
            val result = collectionReference.document()
            result.set(data)
            Resource.Success(result.id)
        } catch (e: Exception) {
            Timber.d( "error: ${e.localizedMessage}")
            Timber.d( "errorcode: ${e.getCode()}")

            Timber.d( "e: $e")
            Resource.Error(R.string.failed)
        }
    }

    override suspend fun removeData(data: AddedCard): Resource<String> {
        return try {
            collectionReference.document(data.id).delete()//.await()
            Resource.Success(data.id)
        } catch (e: Exception) {
            Timber.d( "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    override suspend fun updateData(data: AddedCard): Resource<String> {
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

    suspend fun updateCardProfilePhoto(url: String, cardId: String): Resource<Int> {
        return try {
            collectionReference.document(cardId).update("profilePicUrl", url).await()
            Resource.Success(R.string.success)
        } catch (e: Exception) {
            Timber.d( "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    override fun getData(id: String?): Flow<Resource<AddedCard>> = callbackFlow {

        id?.let { id ->
            trySend(Resource.Loading)

            val subscriptionCallback =
                collectionReference.document(id).addSnapshotListener { value, error ->
                    value?.let {
                        try {
                            if (it.exists())
                                trySend(Resource.Success(it.toObject(AddedCard::class.java)!!))
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

    fun getList(orderField: String): Flow<Resource<List<AddedCard>>> = callbackFlow {

        val tweakedReference = if (orderField == "createdAt")
            collectionReference.orderBy(orderField, Query.Direction.DESCENDING)
        else
            collectionReference.orderBy(orderField)

        trySend(Resource.Loading)
        val subscriptionCallback = tweakedReference.addSnapshotListener { value, error ->
            value?.let {
                try {
                    trySend(Resource.Success(value.toObjects(AddedCard::class.java)))
                } catch (e: Exception) {
                     Timber.d("getData: ${e.localizedMessage}")
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
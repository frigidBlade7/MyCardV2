package com.codedevtech.mycardv2.models.datasource

import android.util.Log
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.models.LiveCard
import com.codedevtech.mycardv2.models.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

abstract class LiveCardDataSource : DataSource<LiveCard> {

    abstract var collectionReference: CollectionReference


    override suspend fun addData(data: LiveCard): Resource<String> {
        return try {
            val result = collectionReference.add(data).await()
            Resource.Success(result.id)
        }catch (e:Exception){
            Log.d(TAG, "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    override suspend fun removeData(data: LiveCard) : Resource<String> {
        return try {
            val result = collectionReference.document(data.id).delete().await()
            Resource.Success(data.id)
        }catch (e:Exception){
            Log.d(TAG, "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    override suspend fun updateData(data: LiveCard): Resource<String> {
        TODO("Not yet implemented, dont forget to handle timestamp")
    }

    suspend fun updateCardProfilePhoto(url: String, cardId: String): Resource<Int> {
        return try {
            collectionReference.document(cardId).update("profilePicUrl",url).await()
            Resource.Success(R.string.success)
        }catch (e:Exception){
            Log.d(TAG, "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }
    suspend fun updateCardCompanyLogo(url: String, cardId: String): Resource<Int> {
        return try {
            collectionReference.document(cardId).update(mapOf("businessInfo.companyLogo" to url)).await()
            Resource.Success(R.string.success)
        }catch (e:Exception){
            Log.d(TAG, "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }    }

    override fun getData(id: String): Flow<Resource<LiveCard>> = callbackFlow {
        offer(Resource.Loading)
        val subscriptionCallback = collectionReference.document(id).addSnapshotListener { value, error ->
            value?.let {
                try {
                    if (it.exists())
                        offer(Resource.Success(it.toObject(LiveCard::class.java)!!))
                    else
                        offer(Resource.Error(R.string.card_not_found))
                }catch (e: Exception){
                    Log.d(TAG, "getData: ${e.localizedMessage}")
                    offer(Resource.Error(R.string.card_not_found))

                }
            }
        }

        awaitClose { subscriptionCallback.remove() }
    }

    fun getList(owner: String): Flow<Resource<List<LiveCard>>> = callbackFlow {

        offer(Resource.Loading)
        val subscriptionCallback = collectionReference.whereEqualTo("owner",owner).orderBy("createdAt",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            value?.let {
                //todo is this necessary if(!it.isEmpty)
                try {
                    //todo save to room

                    offer(Resource.Success(value.toObjects(LiveCard::class.java)))
                }catch (e: Exception){
                    Log.d(TAG, "getData: ${e.localizedMessage}")
                    offer(Resource.Error(R.string.cards_not_found))

                }
            }
        }

        awaitClose { subscriptionCallback.remove() }
    }

/*    class FirestoreCardDeserializer : Function<DocumentSnapshot,Card> {
        override fun apply(input: DocumentSnapshot?): Card? {
            return input?.toObject(Card::class.java)
        }
    }

    class FirestoreCardListDeserializer : Function<QuerySnapshot,List<Card>> {
        override fun apply(input: QuerySnapshot?): List<Card>? {
            return input?.toObjects(Card::class.java)
        }

    }*/
    companion object {
        private const val TAG = "CardDataSource"
    }


}
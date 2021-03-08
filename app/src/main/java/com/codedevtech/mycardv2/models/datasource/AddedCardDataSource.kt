package com.codedevtech.mycardv2.models.datasource

import android.net.ConnectivityManager
import android.util.Log
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.di.HasNetwork
import com.codedevtech.mycardv2.models.AddedCard
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.utils.getCode
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

abstract class AddedCardDataSource : DataSource<AddedCard> {

    abstract var collectionReference: CollectionReference
    //todo review1 abstract var cm: ConnectivityManager



    override suspend fun addData(data: AddedCard): Resource<String?> {
        return try {
            val result = collectionReference.document()
            result.set(data)
            Resource.Success(result.id)
        } catch (e: Exception) {
            Log.d(TAG, "error: ${e.localizedMessage}")
            Log.d(TAG, "errorcode: ${e.getCode()}")

            Log.d(TAG, "e: $e.")
            Resource.Error(R.string.failed)
        }

        /* todo review1 this
            val isOnline = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            cm.getNetworkCapabilities(cm.activeNetwork) != null

        }else{
            cm.activeNetworkInfo !=null
        }

        if(isOnline) {
            return try {
                val result = collectionReference.document()
                result.set(data)
                Resource.Success(result.id)
            } catch (e: Exception) {
                Log.d(TAG, "error: ${e.localizedMessage}")
                Log.d(TAG, "errorcode: ${e.getCode()}")

                Log.d(TAG, "e: $e.")
                Resource.Error(R.string.failed)
            }
        }
        else{
            return try {
                val result = collectionReference.add(data)
                Resource.Success(null)
            } catch (e: Exception) {
                Log.d(TAG, "error: ${e.localizedMessage}")
                Log.d(TAG, "errorcode: ${e.getCode()}")

                Log.d(TAG, "e: $e.")
                Resource.Error(R.string.failed)
            }
        }*/
    }

    override suspend fun removeData(data: AddedCard): Resource<String> {
        return try {
            val result = collectionReference.document(data.id).delete()//.await()
            Resource.Success(data.id)
        }catch (e:Exception){
            Log.d(TAG, "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
    }

    override suspend fun updateData(data: AddedCard): Resource<String> {
        return try {
            Log.d(TAG, "updateData: ${data.id}")
            data.updatedAt = Timestamp.now()

            val result = collectionReference.document(data.id).set(data)//.await()
            Resource.Success(data.id)
        }catch (e:Exception){
            Log.d(TAG, "error: ${e.localizedMessage}")
            Resource.Error(R.string.failed)
        }
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

    override fun getData(id: String): Flow<Resource<AddedCard>> = callbackFlow {
        offer(Resource.Loading)
        val subscriptionCallback = collectionReference.document(id).addSnapshotListener { value, error ->
            value?.let {
                try {
                    if (it.exists())
                        offer(Resource.Success(it.toObject(AddedCard::class.java)!!))
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

    fun getList(orderField: String): Flow<Resource<List<AddedCard>>> = callbackFlow {

        val tweakedReference = if(orderField == "createdAt")
            collectionReference.orderBy(orderField,Query.Direction.DESCENDING)
        else
            collectionReference.orderBy(orderField)

        offer(Resource.Loading)
        val subscriptionCallback = tweakedReference.addSnapshotListener { value, error ->
            value?.let {
                //todo is this necessary if(!it.isEmpty)
                try {
                    offer(Resource.Success(value.toObjects(AddedCard::class.java)))
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
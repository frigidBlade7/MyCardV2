package com.codedevtech.mycardv2.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.codedevtech.mycardv2.models.AddedCard
import com.codedevtech.mycardv2.models.LiveCard
import kotlinx.coroutines.flow.Flow

@Dao
interface AddedCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCard(addedCard: AddedCard)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllCards(addedCardList: List<AddedCard>)

    @Query("SELECT * FROM addedcard ORDER BY createdAt ASC")
    fun sortCardsByRecentFirst(): Flow<List<AddedCard>>


    @Query("SELECT * FROM addedcard ORDER BY fullName ASC")
    fun sortCardsByName(): Flow<List<AddedCard>>


    @Query("SELECT * FROM addedcard WHERE fullName LIKE (:filterQuery) ORDER BY fullName ASC")
    fun filterByName(filterQuery: String): PagingSource<Int,AddedCard>

    @Query("SELECT * FROM addedcard WHERE fullName LIKE (:filterQuery) ORDER BY fullName ASC")
    fun filterByNameToo(filterQuery: String): LiveData<List<AddedCard>>


    @Query("SELECT * FROM addedcard WHERE companyName LIKE (:filterQuery) ORDER BY companyName ASC")
    fun filterByCompany(filterQuery: String): PagingSource<Int,AddedCard>


    @Query("SELECT * FROM addedcard WHERE role LIKE (:filterQuery) ORDER BY role ASC")
    fun filterByRole(filterQuery: String): PagingSource<Int,AddedCard>


    @Query("SELECT * FROM addedcard WHERE addedcard MATCH (:filterQuery)")
    fun filterByAny(filterQuery: String): PagingSource<Int,AddedCard>

    @Query("DELETE FROM addedcard")
    suspend fun deleteAll()
}

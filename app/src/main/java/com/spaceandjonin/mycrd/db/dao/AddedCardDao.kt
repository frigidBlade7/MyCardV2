package com.spaceandjonin.mycrd.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.spaceandjonin.mycrd.models.AddedCard
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


    @Query("SELECT * FROM addedcard WHERE fullName LIKE (:filterQuery) ORDER BY createdAt ASC")
    fun filterByName(filterQuery: String): PagingSource<Int, AddedCard>


    @Query("SELECT * FROM addedcard WHERE companyName LIKE (:filterQuery) ORDER BY createdAt ASC")
    fun filterByCompany(filterQuery: String): PagingSource<Int, AddedCard>


    @Query("SELECT * FROM addedcard WHERE role LIKE (:filterQuery) ORDER BY createdAt ASC")
    fun filterByRole(filterQuery: String): PagingSource<Int, AddedCard>


    @Query(" SELECT * FROM addedcard WHERE addedcard MATCH (:filterQuery) ORDER BY createdAt ASC")
    fun filterByAny(filterQuery: String): PagingSource<Int, AddedCard>

    @Query(" SELECT * FROM addedcard ORDER BY createdAt ASC")
    fun filterByNone(): PagingSource<Int, AddedCard>

    @Query("DELETE FROM addedcard")
    suspend fun deleteAll()
}

package com.spaceandjonin.mycrd.db.dao

import androidx.room.*
import com.spaceandjonin.mycrd.models.LiveCard
import kotlinx.coroutines.flow.Flow

@Dao
interface LiveCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCard(liveCard: LiveCard)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllCards(liveCardList: List<LiveCard>)

    @Query("SELECT * FROM livecard ORDER BY createdAt ASC")
    fun sortCardsByRecentFirst(): Flow<List<LiveCard>>


    @Query("SELECT * FROM livecard ORDER BY fullName ASC")
    fun sortCardsByName(): Flow<List<LiveCard>>


    @Query("SELECT * FROM livecard WHERE fullName LIKE (:filterQuery) ORDER BY fullName ASC")
    fun filterByName(filterQuery: String): Flow<List<LiveCard>>


    @Query("SELECT * FROM livecard WHERE companyName LIKE (:filterQuery) ORDER BY companyName ASC")
    fun filterByCompany(filterQuery: String): Flow<List<LiveCard>>


    @Query("SELECT * FROM livecard WHERE role LIKE (:filterQuery) ORDER BY role ASC")
    fun filterByRole(filterQuery: String): Flow<List<LiveCard>>


    @Query("SELECT * FROM livecard WHERE livecard MATCH (:filterQuery)")
    fun filterByAny(filterQuery: String): Flow<List<LiveCard>>


    @Query("DELETE FROM livecard")
    suspend fun deleteAll()
}

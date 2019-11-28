package com.openpayd.data.room.dao

import androidx.room.*
import com.openpayd.data.room.model.ResultsModel

@Dao
interface ResultsDao {

    @Query("SELECT * FROM searchResults WHERE id = :id")
    suspend fun getResult(id: String): ResultsModel?

    @Query("SELECT * FROM searchResults")
    suspend fun getResults(): List<ResultsModel>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: ResultsModel)

    @Update
    suspend fun updateResult(result: ResultsModel)

    @Delete
    suspend fun deleteResult(result: ResultsModel)

    @Query("DELETE FROM searchResults")
    suspend fun deleteAll()
}

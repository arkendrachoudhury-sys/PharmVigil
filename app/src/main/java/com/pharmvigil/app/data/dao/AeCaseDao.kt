package com.pharmvigil.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pharmvigil.app.data.model.AeCase
import kotlinx.coroutines.flow.Flow

@Dao
interface AeCaseDao {
    @Query("SELECT * FROM ae_cases ORDER BY reportedTimestamp DESC")
    fun getAllCases(): Flow<List<AeCase>>

    @Query("SELECT * FROM ae_cases WHERE grade = :grade ORDER BY reportedTimestamp DESC")
    fun getCasesByGrade(grade: Int): Flow<List<AeCase>>

    @Query("SELECT * FROM ae_cases WHERE isSerious = :isSerious ORDER BY reportedTimestamp DESC")
    fun getCasesBySeriousness(isSerious: Boolean): Flow<List<AeCase>>

    @Query("SELECT * FROM ae_cases WHERE id = :id")
    suspend fun getCaseById(id: Long): AeCase?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(aeCase: AeCase): Long

    @Update
    suspend fun updateCase(aeCase: AeCase)

    @Delete
    suspend fun deleteCase(aeCase: AeCase)

    @Query("DELETE FROM ae_cases WHERE id = :id")
    suspend fun deleteCaseById(id: Long)

    @Query("SELECT COUNT(*) FROM ae_cases")
    fun getCaseCount(): Flow<Int>
}

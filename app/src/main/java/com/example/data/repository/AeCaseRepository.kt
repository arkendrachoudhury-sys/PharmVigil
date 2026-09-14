package com.example.data.repository

import com.example.data.dao.AeCaseDao
import com.example.data.model.AeCase
import kotlinx.coroutines.flow.Flow

class AeCaseRepository(private val dao: AeCaseDao) {
    val allCases: Flow<List<AeCase>> = dao.getAllCases()
    val caseCount: Flow<Int> = dao.getCaseCount()

    fun getCasesByGrade(grade: Int): Flow<List<AeCase>> = dao.getCasesByGrade(grade)
    fun getCasesBySeriousness(isSerious: Boolean): Flow<List<AeCase>> = dao.getCasesBySeriousness(isSerious)

    suspend fun insert(aeCase: AeCase): Long = dao.insertCase(aeCase)
    suspend fun update(aeCase: AeCase) = dao.updateCase(aeCase)
    suspend fun delete(aeCase: AeCase) = dao.deleteCase(aeCase)
    suspend fun deleteById(id: Long) = dao.deleteCaseById(id)
    suspend fun getById(id: Long): AeCase? = dao.getCaseById(id)
}

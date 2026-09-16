package com.pharmvigil.app.data.repository

import com.pharmvigil.app.data.dao.AeCaseDao
import com.pharmvigil.app.data.model.AeCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AeCaseRepository(private val dao: AeCaseDao) {
    val allCases: Flow<List<AeCase>> = dao.getAllCases()
    val caseCount: Flow<Int> = dao.getCaseCount()

    fun getCasesByGrade(grade: Int): Flow<List<AeCase>> = dao.getCasesByGrade(grade)
    fun getCasesBySeriousness(isSerious: Boolean): Flow<List<AeCase>> = dao.getCasesBySeriousness(isSerious)

    suspend fun insert(aeCase: AeCase): Long = withContext(Dispatchers.IO) { dao.insertCase(aeCase) }
    suspend fun update(aeCase: AeCase) = withContext(Dispatchers.IO) { dao.updateCase(aeCase) }
    suspend fun delete(aeCase: AeCase) = withContext(Dispatchers.IO) { dao.deleteCase(aeCase) }
    suspend fun deleteById(id: Long) = withContext(Dispatchers.IO) { dao.deleteCaseById(id) }
    suspend fun getById(id: Long): AeCase? = withContext(Dispatchers.IO) { dao.getCaseById(id) }
}

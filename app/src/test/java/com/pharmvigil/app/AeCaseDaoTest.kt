package com.pharmvigil.app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.pharmvigil.app.data.AppDatabase
import com.pharmvigil.app.data.dao.AeCaseDao
import com.pharmvigil.app.data.model.AeCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AeCaseDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: AeCaseDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.aeCaseDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndGetCase() = runBlocking {
        val case = AeCase(
            patientId = "PAT-001",
            suspectDrug = "DrugA",
            adverseEventTerm = "Diarrhea",
            medDraPt = "Diarrhea",
            systemOrganClass = "Gastrointestinal disorders",
            grade = 2,
            gradeDescription = "Moderate",
            onsetDate = "2026-09-01",
            isSerious = false
        )
        val id = dao.insertCase(case)
        val loaded = dao.getCaseById(id)
        assertNotNull(loaded)
        assertEquals("PAT-001", loaded?.patientId)
        assertEquals("Diarrhea", loaded?.adverseEventTerm)
    }

    @Test
    fun testUpdateCase() = runBlocking {
        val case = AeCase(
            patientId = "PAT-002",
            suspectDrug = "DrugB",
            adverseEventTerm = "Nausea",
            medDraPt = "Nausea",
            systemOrganClass = "Gastrointestinal disorders",
            grade = 1,
            gradeDescription = "Mild",
            onsetDate = "2026-09-01",
            isSerious = false
        )
        val id = dao.insertCase(case)
        val loaded = dao.getCaseById(id)!!

        val updated = loaded.copy(grade = 3, isSerious = true)
        dao.updateCase(updated)

        val reloaded = dao.getCaseById(id)!!
        assertEquals(3, reloaded.grade)
        assertEquals(true, reloaded.isSerious)
    }

    @Test
    fun testDeleteCase() = runBlocking {
        val case = AeCase(
            patientId = "PAT-003",
            suspectDrug = "DrugC",
            adverseEventTerm = "Fatigue",
            medDraPt = "Fatigue",
            systemOrganClass = "General disorders",
            grade = 1,
            gradeDescription = "Mild",
            onsetDate = "2026-09-01",
            isSerious = false
        )
        val id = dao.insertCase(case)
        dao.deleteCaseById(id)
        val loaded = dao.getCaseById(id)
        assertNull(loaded)
    }

    @Test
    fun testGetAllCasesFlow() = runBlocking {
        val case1 = AeCase(patientId = "PAT-1", suspectDrug = "A", adverseEventTerm = "Colitis", medDraPt = "Colitis", systemOrganClass = "GI", grade = 3, gradeDescription = "", onsetDate = "2026-09-01", isSerious = true)
        val case2 = AeCase(patientId = "PAT-2", suspectDrug = "B", adverseEventTerm = "Rash", medDraPt = "Rash", systemOrganClass = "Skin", grade = 1, gradeDescription = "", onsetDate = "2026-09-02", isSerious = false)

        dao.insertCase(case1)
        dao.insertCase(case2)

        val cases = dao.getAllCases().first()
        assertEquals(2, cases.size)
    }
}

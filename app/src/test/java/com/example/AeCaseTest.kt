package com.example

import com.example.data.model.AeCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AeCaseTest {
    @Test
    fun testAeCaseCreation() {
        val case = AeCase(
            patientId = "PAT-001",
            suspectDrug = "DrugA",
            adverseEventTerm = "Nausea",
            medDraPt = "Nausea",
            systemOrganClass = "Gastrointestinal disorders",
            grade = 2,
            gradeDescription = "Moderate",
            onsetDate = "2026-09-01",
            isSerious = false
        )
        assertEquals("PAT-001", case.patientId)
        assertEquals("Nausea", case.adverseEventTerm)
        assertEquals(2, case.grade)
    }
}

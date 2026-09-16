package com.pharmvigil.app

import com.pharmvigil.app.data.model.AeCase
import com.pharmvigil.app.data.model.AeCaseValidator
import com.pharmvigil.app.data.model.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AeCaseValidatorTest {

    @Test
    fun testValidCase() {
        val validCase = AeCase(
            patientId = "PAT-101",
            suspectDrug = "Pembrolizumab",
            adverseEventTerm = "Colitis",
            medDraPt = "Colitis",
            systemOrganClass = "Gastrointestinal disorders",
            grade = 3,
            gradeDescription = "Severe",
            onsetDate = "2026-09-01",
            isSerious = true
        )
        val result = AeCaseValidator.validate(validCase)
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun testInvalidBlankFieldsAndInvalidGrade() {
        val invalidCase = AeCase(
            patientId = "",
            suspectDrug = "",
            adverseEventTerm = "",
            medDraPt = "",
            systemOrganClass = "",
            grade = 6, // Invalid grade
            gradeDescription = "",
            onsetDate = "",
            isSerious = false
        )
        val result = AeCaseValidator.validate(invalidCase)
        assertTrue(result is ValidationResult.Invalid)
        val errors = (result as ValidationResult.Invalid).errors
        assertEquals(5, errors.size)
    }
}

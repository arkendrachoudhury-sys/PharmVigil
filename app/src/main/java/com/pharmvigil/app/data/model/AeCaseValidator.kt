package com.pharmvigil.app.data.model

sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val errors: List<String>) : ValidationResult()
}

object AeCaseValidator {
    fun validate(case: AeCase): ValidationResult {
        val errors = mutableListOf<String>()

        if (case.patientId.isBlank()) {
            errors.add("Subject/Patient ID is required.")
        }
        if (case.suspectDrug.isBlank()) {
            errors.add("Suspect Drug name is required.")
        }
        if (case.adverseEventTerm.isBlank()) {
            errors.add("Adverse Event Term is required.")
        }
        if (case.grade !in 1..5) {
            errors.add("CTCAE Grade must be an integer between 1 and 5.")
        }
        if (case.onsetDate.isBlank()) {
            errors.add("Event Onset Date is required.")
        }

        return if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)
    }
}

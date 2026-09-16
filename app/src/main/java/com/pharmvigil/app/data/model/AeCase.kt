package com.pharmvigil.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ae_cases")
data class AeCase(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val patientId: String,
    val protocolId: String = "CLIN-2026-01",
    val suspectDrug: String,
    val adverseEventTerm: String,
    val medDraPt: String,
    val systemOrganClass: String,
    val grade: Int, // 1 to 5
    val gradeDescription: String,
    val onsetDate: String,
    val isSerious: Boolean,
    val seriousnessCriteria: String = "", // e.g., "Hospitalization, Life-threatening"
    val causality: String = "Possible", // Definite, Probable, Possible, Unlikely, Unrelated
    val outcome: String = "Recovering/Resolving", // Recovered, Recovering, Not Recovered, Fatal, Unknown
    val actionTakenWithDrug: String = "Dose Interrupted", // Dose Reduced, Interrupted, Discontinued, Dose Not Changed
    val clinicalNotes: String = "",
    val reportedTimestamp: Long = System.currentTimeMillis()
)

package com.pharmvigil.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ae_cases")
data class AeCase(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val patientId: String,
    val protocolId: String = "",
    val suspectDrug: String,
    val adverseEventTerm: String,
    val medDraPt: String,
    val systemOrganClass: String,
    val grade: Int, // 1 to 5
    val gradeDescription: String,
    val onsetDate: String,
    val isSerious: Boolean,
    val seriousnessCriteria: String = "",
    val causality: String = Causality.POSSIBLE.label,
    val outcome: String = Outcome.RECOVERING.label,
    val actionTakenWithDrug: String = ActionTaken.DOSE_INTERRUPTED.label,
    val clinicalNotes: String = "",
    val reportedTimestamp: Long = System.currentTimeMillis(),
    val isDemo: Boolean = false
)

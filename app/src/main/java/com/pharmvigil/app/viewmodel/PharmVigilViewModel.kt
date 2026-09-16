package com.pharmvigil.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pharmvigil.app.data.AppDatabase
import com.pharmvigil.app.data.DemoData
import com.pharmvigil.app.data.ctcae.CtcaeDatabase
import com.pharmvigil.app.data.ctcae.CtcaeTerm
import com.pharmvigil.app.data.model.AeCase
import com.pharmvigil.app.data.repository.AeCaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PharmVigilViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AeCaseRepository

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = AeCaseRepository(db.aeCaseDao())
    }

    // Filter states
    val searchQuery = MutableStateFlow("")
    val selectedGradeFilter = MutableStateFlow<Int?>(null) // null = all
    val selectedSeriousnessFilter = MutableStateFlow<Boolean?>(null) // null = all

    // Cases stream
    val allCases: StateFlow<List<AeCase>> = repository.allCases
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered cases
    val filteredCases: StateFlow<List<AeCase>> = combine(
        allCases,
        searchQuery,
        selectedGradeFilter,
        selectedSeriousnessFilter
    ) { cases, query, grade, isSerious ->
        cases.filter { case ->
            val matchesQuery = query.isBlank() ||
                case.adverseEventTerm.contains(query, ignoreCase = true) ||
                case.patientId.contains(query, ignoreCase = true) ||
                case.suspectDrug.contains(query, ignoreCase = true) ||
                case.medDraPt.contains(query, ignoreCase = true) ||
                case.systemOrganClass.contains(query, ignoreCase = true)

            val matchesGrade = grade == null || case.grade == grade
            val matchesSeriousness = isSerious == null || case.isSerious == isSerious

            matchesQuery && matchesGrade && matchesSeriousness
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // CTCAE Catalog Search
    val ctcaeSearchQuery = MutableStateFlow("")
    val selectedSocFilter = MutableStateFlow<String?>(null)

    val ctcaeResults: StateFlow<List<CtcaeTerm>> = combine(
        ctcaeSearchQuery,
        selectedSocFilter
    ) { query, soc ->
        val results = CtcaeDatabase.search(query)
        if (soc == null) results else results.filter { it.soc == soc }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CtcaeDatabase.terms)

    val allSocs: List<String> = CtcaeDatabase.getAllSocs()

    // Operations
    fun addCase(aeCase: AeCase) {
        viewModelScope.launch {
            repository.insert(aeCase)
        }
    }

    fun deleteCase(aeCase: AeCase) {
        viewModelScope.launch {
            repository.delete(aeCase)
        }
    }

    fun deleteCaseById(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun loadDemoData() {
        viewModelScope.launch {
            DemoData.sampleCases.forEach { repository.insert(it) }
        }
    }

    // CIOMS I / Regulatory Narrative generator
    fun generateCiomsNarrative(case: AeCase): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val formattedReportTime = dateFormat.format(Date(case.reportedTimestamp))
        val seriousnessText = if (case.isSerious) "SERIOUS ADVERSE EVENT (SAE)" else "NON-SERIOUS ADVERSE EVENT (AE)"

        return """
=====================================================
CIOMS-I / ICH E2A EXPEDITED SAFETY REPORT SUMMARY
=====================================================
Classification: $seriousnessText
Protocol ID:    ${case.protocolId}
Subject ID:     ${case.patientId}
Report Date:    $formattedReportTime

I. REACTION INFORMATION
-----------------------
Adverse Event:  ${case.adverseEventTerm}
MedDRA PT:      ${case.medDraPt}
System Organ:   ${case.systemOrganClass}
CTCAE Severity: Grade ${case.grade} (${case.gradeDescription})
Onset Date:     ${case.onsetDate}
Outcome:        ${case.outcome}

II. SUSPECT DRUG INFORMATION
----------------------------
Suspect Agent:  ${case.suspectDrug}
Action Taken:   ${case.actionTakenWithDrug}
Causality:      ${case.causality}

III. SERIOUSNESS CRITERIA (ICH E2A / FDA 21 CFR 312.32)
-------------------------------------------------------
Serious Event:  ${if (case.isSerious) "YES" else "NO"}
Criteria Met:   ${if (case.seriousnessCriteria.isNotBlank()) case.seriousnessCriteria else "None"}

IV. CLINICAL NARRATIVE
----------------------
${case.clinicalNotes.ifBlank { "No additional clinical notes recorded." }}
=====================================================
Generated by PharmVigil Clinical Regulatory Informatics
        """.trimIndent()
    }
}

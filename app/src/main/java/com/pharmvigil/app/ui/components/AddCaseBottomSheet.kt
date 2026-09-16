package com.pharmvigil.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pharmvigil.app.data.ctcae.CtcaeDatabase
import com.pharmvigil.app.data.ctcae.CtcaeTerm
import com.pharmvigil.app.data.model.AeCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCaseBottomSheet(
    preselectedTerm: CtcaeTerm? = null,
    onDismiss: () -> Unit,
    onSubmit: (AeCase) -> Unit
) {
    var patientId by remember { mutableStateOf("") }
    var protocolId by remember { mutableStateOf("ONC-2026-01") }
    var suspectDrug by remember { mutableStateOf("") }
    var adverseEventTerm by remember { mutableStateOf(preselectedTerm?.term ?: "") }
    var medDraPt by remember { mutableStateOf(preselectedTerm?.medDraPt ?: "") }
    var soc by remember { mutableStateOf(preselectedTerm?.soc ?: "Gastrointestinal disorders") }
    var selectedGrade by remember { mutableIntStateOf(1) }

    // Seriousness Criteria checklist
    var isDeath by remember { mutableStateOf(false) }
    var isLifeThreatening by remember { mutableStateOf(false) }
    var isHospitalization by remember { mutableStateOf(false) }
    var isDisability by remember { mutableStateOf(false) }
    var isCongenital by remember { mutableStateOf(false) }
    var isImportantMedicalEvent by remember { mutableStateOf(false) }

    var causality by remember { mutableStateOf("Possible") }
    var outcome by remember { mutableStateOf("Recovering/Resolving") }
    var drugAction by remember { mutableStateOf("Dose Interrupted") }
    var clinicalNotes by remember { mutableStateOf("") }

    // Matching CTCAE item
    val matchedCtcae = remember(adverseEventTerm) {
        CtcaeDatabase.terms.find { it.term.equals(adverseEventTerm, ignoreCase = true) }
    }

    // Auto update seriousness flags if Grade 5 (Death) or Grade 4 (Life-threatening) selected
    LaunchedEffect(selectedGrade) {
        if (selectedGrade == 5) {
            isDeath = true
            outcome = "Fatal"
        } else if (selectedGrade == 4) {
            isLifeThreatening = true
        }
    }

    val isSerious = isDeath || isLifeThreatening || isHospitalization || isDisability || isCongenital || isImportantMedicalEvent

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.fillMaxHeight(0.92f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Log Adverse Event / SAE",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            // Patient and Protocol Identification
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = patientId,
                    onValueChange = { patientId = it },
                    label = { Text("Subject ID *") },
                    placeholder = { Text("e.g. SUBJ-1082") },
                    modifier = Modifier.weight(1f).testTag("patient_id_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = protocolId,
                    onValueChange = { protocolId = it },
                    label = { Text("Protocol ID") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Suspect Drug
            OutlinedTextField(
                value = suspectDrug,
                onValueChange = { suspectDrug = it },
                label = { Text("Suspect Drug / Agent *") },
                placeholder = { Text("e.g. Pembrolizumab, Cisplatin, Doxorubicin") },
                modifier = Modifier.fillMaxWidth().testTag("suspect_drug_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Adverse Event Term
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedTextField(
                    value = adverseEventTerm,
                    onValueChange = { input ->
                        adverseEventTerm = input
                        val match = CtcaeDatabase.terms.find { it.term.contains(input, ignoreCase = true) }
                        if (match != null && input.isNotBlank()) {
                            medDraPt = match.medDraPt
                            soc = match.soc
                        }
                    },
                    label = { Text("Adverse Event Term (CTCAE / MedDRA) *") },
                    placeholder = { Text("e.g. Diarrhea, Colitis, Pneumonitis") },
                    modifier = Modifier.fillMaxWidth().testTag("ae_term_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                // Quick suggestions from preloaded CTCAE library
                if (adverseEventTerm.isBlank()) {
                    Text(
                        "Common clinical terms:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Diarrhea", "Colitis", "Pneumonitis", "Fatigue").forEach { term ->
                            SuggestionChip(
                                onClick = {
                                    adverseEventTerm = term
                                    val item = CtcaeDatabase.terms.find { it.term == term }
                                    if (item != null) {
                                        medDraPt = item.medDraPt
                                        soc = item.soc
                                    }
                                },
                                label = { Text(term, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }
            }

            // CTCAE Severity Selector
            Text(
                "CTCAE Severity Level (Grade 1 - 5)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground
            )

            // Grade selection buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (g in 1..5) {
                    val isSelected = selectedGrade == g
                    val gradeColor = getGradeColor(g)
                    val label = getGradeShortLabel(g)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) gradeColor.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) gradeColor else MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedGrade = g }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "G$g",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) gradeColor else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) gradeColor else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Display official CTCAE criteria for selected grade
            val currentGradeCriteria = when (selectedGrade) {
                1 -> matchedCtcae?.grade1 ?: "Mild; asymptomatic or mild symptoms; clinical or diagnostic observations only; intervention not indicated."
                2 -> matchedCtcae?.grade2 ?: "Moderate; minimal, local or noninvasive intervention indicated; limiting age-appropriate instrumental ADL."
                3 -> matchedCtcae?.grade3 ?: "Severe or medically significant but not immediately life-threatening; hospitalization or prolongation of hospitalization indicated; disabling; limiting self care ADL."
                4 -> matchedCtcae?.grade4 ?: "Life-threatening consequences; urgent intervention indicated."
                5 -> matchedCtcae?.grade5 ?: "Death related to AE."
                else -> ""
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = getGradeColor(selectedGrade),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CTCAE Criteria for Grade $selectedGrade:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = getGradeColor(selectedGrade)
                        )
                    }
                    Text(
                        text = currentGradeCriteria,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Seriousness Assessment (ICH E2A)
            Text(
                "Regulatory Seriousness Criteria (ICH E2A / FDA 21 CFR 312.32)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    SeriousnessCheckbox("Results in Death", isDeath) { isDeath = it }
                    SeriousnessCheckbox("Life-Threatening", isLifeThreatening) { isLifeThreatening = it }
                    SeriousnessCheckbox("Requires or Prolongs Inpatient Hospitalization", isHospitalization) { isHospitalization = it }
                    SeriousnessCheckbox("Persistent or Significant Disability/Incapacity", isDisability) { isDisability = it }
                    SeriousnessCheckbox("Congenital Anomaly / Birth Defect", isCongenital) { isCongenital = it }
                    SeriousnessCheckbox("Other Medically Important Event (IME)", isImportantMedicalEvent) { isImportantMedicalEvent = it }
                }
            }

            // Seriousness Result Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Calculated Classification:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                SeriousnessBadge(isSerious = isSerious)
            }

            // Causality & Action
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Causality
                Column(modifier = Modifier.weight(1f)) {
                    Text("Causality Assessment", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    var expanded by remember { mutableStateOf(false) }
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(causality, maxLines = 1)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listOf("Definite", "Probable", "Possible", "Unlikely", "Unrelated").forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    causality = opt
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Action taken
                Column(modifier = Modifier.weight(1f)) {
                    Text("Action with Drug", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    var expandedAction by remember { mutableStateOf(false) }
                    OutlinedButton(
                        onClick = { expandedAction = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(drugAction, maxLines = 1)
                    }
                    DropdownMenu(expanded = expandedAction, onDismissRequest = { expandedAction = false }) {
                        listOf("Dose Not Changed", "Dose Reduced", "Dose Interrupted", "Permanently Discontinued").forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    drugAction = opt
                                    expandedAction = false
                                }
                            )
                        }
                    }
                }
            }

            // Clinical Notes
            OutlinedTextField(
                value = clinicalNotes,
                onValueChange = { clinicalNotes = it },
                label = { Text("Clinical Narrative / Notes") },
                placeholder = { Text("Provide clinical context, interventions administered, concomitant medications...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6,
                shape = RoundedCornerShape(12.dp)
            )

            // Submit Button
            val canSubmit = patientId.isNotBlank() && suspectDrug.isNotBlank() && adverseEventTerm.isNotBlank()
            Button(
                onClick = {
                    if (canSubmit) {
                        val criteriaList = mutableListOf<String>()
                        if (isDeath) criteriaList.add("Death")
                        if (isLifeThreatening) criteriaList.add("Life-threatening")
                        if (isHospitalization) criteriaList.add("Hospitalization")
                        if (isDisability) criteriaList.add("Disability")
                        if (isCongenital) criteriaList.add("Congenital anomaly")
                        if (isImportantMedicalEvent) criteriaList.add("Important medical event")

                        val newCase = AeCase(
                            patientId = patientId.trim(),
                            protocolId = protocolId.trim(),
                            suspectDrug = suspectDrug.trim(),
                            adverseEventTerm = adverseEventTerm.trim(),
                            medDraPt = medDraPt.ifBlank { adverseEventTerm.trim() },
                            systemOrganClass = soc,
                            grade = selectedGrade,
                            gradeDescription = currentGradeCriteria,
                            onsetDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                            isSerious = isSerious,
                            seriousnessCriteria = criteriaList.joinToString(", ").ifBlank { "None" },
                            causality = causality,
                            outcome = outcome,
                            actionTakenWithDrug = drugAction,
                            clinicalNotes = clinicalNotes.trim()
                        )
                        onSubmit(newCase)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_case_button"),
                enabled = canSubmit,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Case Locally (Offline)", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}

@Composable
private fun SeriousnessCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFFE53935)
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

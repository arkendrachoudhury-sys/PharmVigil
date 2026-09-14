package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SaeWizardScreen(
    onStartCaseWithCriteria: () -> Unit
) {
    var isDeath by remember { mutableStateOf(false) }
    var isLifeThreatening by remember { mutableStateOf(false) }
    var isHospitalization by remember { mutableStateOf(false) }
    var isDisability by remember { mutableStateOf(false) }
    var isCongenital by remember { mutableStateOf(false) }
    var isImportantMedicalEvent by remember { mutableStateOf(false) }

    var isRelated by remember { mutableStateOf(true) } // Reasonable causal relationship
    var isExpected by remember { mutableStateOf(false) } // Listed in IB/SmPC

    val isSerious = isDeath || isLifeThreatening || isHospitalization || isDisability || isCongenital || isImportantMedicalEvent
    val isSusar = isSerious && isRelated && !isExpected
    val isFatalOrLifeThreateningSusar = isSusar && (isDeath || isLifeThreatening)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Introduction Header
        Text(
            text = "ICH E2A / FDA SAE Decision Matrix",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Evaluate adverse event seriousness, causality, and regulatory expedited reporting submission clocks according to ICH E2A & FDA 21 CFR 312.32.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Step 1: Seriousness Criteria Checklist
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "1. Seriousness Criteria Checklist",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Check all that apply to this adverse event:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                SeriousnessOption(
                    title = "Results in Death",
                    subtitle = "Patient died as a direct or indirect consequence of the event.",
                    checked = isDeath,
                    onCheckedChange = { isDeath = it }
                )
                SeriousnessOption(
                    title = "Life-Threatening",
                    subtitle = "Patient was at immediate risk of death at the time of the event (not hypothetically).",
                    checked = isLifeThreatening,
                    onCheckedChange = { isLifeThreatening = it }
                )
                SeriousnessOption(
                    title = "Inpatient Hospitalization or Prolongation",
                    subtitle = "Required admission to hospital (>=24h) or prolonged existing inpatient stay.",
                    checked = isHospitalization,
                    onCheckedChange = { isHospitalization = it }
                )
                SeriousnessOption(
                    title = "Persistent or Significant Disability",
                    subtitle = "Substantial disruption of a person's ability to conduct normal life functions.",
                    checked = isDisability,
                    onCheckedChange = { isDisability = it }
                )
                SeriousnessOption(
                    title = "Congenital Anomaly / Birth Defect",
                    subtitle = "Adverse outcome in offspring associated with parental drug exposure.",
                    checked = isCongenital,
                    onCheckedChange = { isCongenital = it }
                )
                SeriousnessOption(
                    title = "Important Medical Event (IME)",
                    subtitle = "Medical judgment indicates urgent intervention needed to prevent the above outcomes.",
                    checked = isImportantMedicalEvent,
                    onCheckedChange = { isImportantMedicalEvent = it }
                )
            }
        }

        // Step 2: Causality & Expectedness
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "2. Causality & Expectedness",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )

                // Causality Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isRelated = !isRelated }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Reasonable Causal Relationship?", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onBackground)
                        Text("Investigator / Sponsor suspects drug attribution (Possible / Probable / Definite)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = isRelated, onCheckedChange = { isRelated = it })
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                // Expectedness Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpected = !isExpected }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Expected in Reference Safety Info (RSI)?", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onBackground)
                        Text("Listed with consistent severity/frequency in Investigator's Brochure (IB) or SmPC", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = isExpected, onCheckedChange = { isExpected = it })
                }
            }
        }

        // Step 3: Regulatory Determination Output Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    isFatalOrLifeThreateningSusar -> Color(0xFFB71C1C).copy(alpha = 0.25f)
                    isSusar -> Color(0xFFFB8C00).copy(alpha = 0.25f)
                    isSerious -> Color(0xFFE53935).copy(alpha = 0.15f)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            ),
            border = BorderStroke(
                width = 1.5.dp,
                color = when {
                    isFatalOrLifeThreateningSusar -> Color(0xFFE53935)
                    isSusar -> Color(0xFFFB8C00)
                    isSerious -> Color(0xFFFFB4AB)
                    else -> MaterialTheme.colorScheme.outline
                }
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSerious) Icons.Default.Warning else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (isSerious) Color(0xFFE53935) else Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSerious) "SERIOUS ADVERSE EVENT (SAE)" else "NON-SERIOUS ADVERSE EVENT (AE)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isSerious) Color(0xFFFFDAD6) else MaterialTheme.colorScheme.onBackground
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                // SUSAR Classification
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("SUSAR Status:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = if (isSusar) "YES (Suspected Unexpected SAE)" else "NO",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isSusar) Color(0xFFFB8C00) else MaterialTheme.colorScheme.onBackground
                    )
                }

                // Regulatory Clock
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Regulatory Clock:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        text = when {
                            isFatalOrLifeThreateningSusar -> "7 CALENDAR DAYS (Expedited Initial)"
                            isSusar -> "15 CALENDAR DAYS (Expedited Alert)"
                            else -> "Periodic (DSUR / Annual Report)"
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = when {
                            isFatalOrLifeThreateningSusar -> Color(0xFFE53935)
                            isSusar -> Color(0xFFFB8C00)
                            else -> MaterialTheme.colorScheme.onBackground
                        }
                    )
                }

                if (isFatalOrLifeThreateningSusar) {
                    Text(
                        text = "NOTE: Initial notification to FDA / Competent Authorities must occur within 7 calendar days, followed by a complete follow-up report within 8 additional days.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFFDAD6)
                    )
                }
            }
        }

        // Action Button
        Button(
            onClick = onStartCaseWithCriteria,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Open Case Logger")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SeriousnessOption(
    title: String,
    subtitle: String,
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
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFFE53935))
        )
        Column(modifier = Modifier.padding(start = 6.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

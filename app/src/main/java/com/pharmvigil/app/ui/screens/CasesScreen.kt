package com.pharmvigil.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pharmvigil.app.data.model.AeCase
import com.pharmvigil.app.ui.components.CaseDetailDialog
import com.pharmvigil.app.ui.components.GradeBadge
import com.pharmvigil.app.ui.components.SeriousnessBadge
import com.pharmvigil.app.ui.components.getGradeColor
import com.pharmvigil.app.viewmodel.PharmVigilViewModel

@Composable
fun CasesScreen(
    viewModel: PharmVigilViewModel,
    onAddNewCase: () -> Unit
) {
    val cases by viewModel.filteredCases.collectAsStateWithLifecycle()
    val allCases by viewModel.allCases.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedGrade by viewModel.selectedGradeFilter.collectAsStateWithLifecycle()
    val selectedSeriousness by viewModel.selectedSeriousnessFilter.collectAsStateWithLifecycle()

    var activeCaseForDetail by remember { mutableStateOf<AeCase?>(null) }

    val totalCases = allCases.size
    val seriousCases = allCases.count { it.isSerious }
    val criticalCases = allCases.count { it.grade >= 4 }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNewCase,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("fab_add_case")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Log Adverse Event")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header stats banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatPill(label = "Total AEs", value = totalCases.toString(), color = MaterialTheme.colorScheme.primary)
                StatPill(label = "Serious (SAE)", value = seriousCases.toString(), color = Color(0xFFE53935))
                StatPill(label = "G4/G5 Critical", value = criticalCases.toString(), color = Color(0xFFB71C1C))
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchQuery.value = it },
                placeholder = { Text("Search by Subject, Drug, Term, or SOC...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Chips
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedGrade == null && selectedSeriousness == null,
                        onClick = {
                            viewModel.selectedGradeFilter.value = null
                            viewModel.selectedSeriousnessFilter.value = null
                        },
                        label = { Text("All Cases") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedSeriousness == true,
                        onClick = {
                            viewModel.selectedSeriousnessFilter.value = if (selectedSeriousness == true) null else true
                        },
                        label = { Text("SAE Only") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFE53935).copy(alpha = 0.25f),
                            selectedLabelColor = Color(0xFFFFB4AB)
                        )
                    )
                }
                for (g in 1..5) {
                    item {
                        FilterChip(
                            selected = selectedGrade == g,
                            onClick = {
                                viewModel.selectedGradeFilter.value = if (selectedGrade == g) null else g
                            },
                            label = { Text("G$g") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = getGradeColor(g).copy(alpha = 0.25f),
                                selectedLabelColor = getGradeColor(g)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cases List
            if (cases.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            if (allCases.isEmpty()) "No adverse event records logged." else "No adverse event records match.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Tap + to log a new clinical case.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                        if (allCases.isEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = { viewModel.loadDemoData() },
                                modifier = Modifier.testTag("load_demo_data_button")
                            ) {
                                Text("Load Sample Cases")
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(cases, key = { it.id }) { item ->
                        CaseCard(case = item, onClick = { activeCaseForDetail = item })
                    }
                }
            }
        }
    }

    // Detail modal dialog
    activeCaseForDetail?.let { caseItem ->
        CaseDetailDialog(
            case = caseItem,
            ciomsNarrative = viewModel.generateCiomsNarrative(caseItem),
            onDismiss = { activeCaseForDetail = null },
            onDelete = {
                viewModel.deleteCase(caseItem)
                activeCaseForDetail = null
            }
        )
    }
}

@Composable
private fun CaseCard(
    case: AeCase,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("case_card_${case.patientId}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Subject ID & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = case.patientId,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Onset: ${case.onsetDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Adverse Event Term
            Text(
                text = case.adverseEventTerm,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Suspect Drug
            Text(
                text = "Drug: ${case.suspectDrug}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            // Badges row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GradeBadge(grade = case.grade)
                SeriousnessBadge(isSerious = case.isSerious)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = case.causality,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RowScope.StatPill(label: String, value: String, color: Color) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(vertical = 6.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

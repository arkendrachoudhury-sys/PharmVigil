package com.pharmvigil.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pharmvigil.app.data.ctcae.CtcaeTerm
import com.pharmvigil.app.ui.components.getGradeColor
import com.pharmvigil.app.ui.components.getGradeShortLabel
import com.pharmvigil.app.viewmodel.PharmVigilViewModel

@Composable
fun CtcaeCatalogScreen(
    viewModel: PharmVigilViewModel,
    onLogTerm: (CtcaeTerm) -> Unit
) {
    val results by viewModel.ctcaeResults.collectAsStateWithLifecycle()
    val searchQuery by viewModel.ctcaeSearchQuery.collectAsStateWithLifecycle()
    val selectedSoc by viewModel.selectedSocFilter.collectAsStateWithLifecycle()
    val allSocs = viewModel.allSocs

    var expandedTermName by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.ctcaeSearchQuery.value = it },
            placeholder = { Text("Search CTCAE terms (e.g. Diarrhea, ALT, Neuropathy)...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { viewModel.ctcaeSearchQuery.value = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

        // System Organ Class (SOC) Filters
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedSoc == null,
                    onClick = { viewModel.selectedSocFilter.value = null },
                    label = { Text("All SOCs (${results.size})") }
                )
            }
            items(allSocs) { soc ->
                FilterChip(
                    selected = selectedSoc == soc,
                    onClick = {
                        viewModel.selectedSocFilter.value = if (selectedSoc == soc) null else soc
                    },
                    label = { Text(soc) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Catalog List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(results, key = { it.term }) { ctcaeItem ->
                val isExpanded = expandedTermName == ctcaeItem.term
                CtcaeTermCard(
                    item = ctcaeItem,
                    isExpanded = isExpanded,
                    onToggleExpand = {
                        expandedTermName = if (isExpanded) null else ctcaeItem.term
                    },
                    onLogEvent = { onLogTerm(ctcaeItem) }
                )
            }
        }
    }
}

@Composable
private fun CtcaeTermCard(
    item: CtcaeTerm,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onLogEvent: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggleExpand),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.term,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "MedDRA PT: ${item.medDraPt} • ${item.soc}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = item.definition,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                    Text(
                        "Official CTCAE Severity Criteria:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    GradeCriteriaRow(grade = 1, criteria = item.grade1)
                    GradeCriteriaRow(grade = 2, criteria = item.grade2)
                    GradeCriteriaRow(grade = 3, criteria = item.grade3)
                    GradeCriteriaRow(grade = 4, criteria = item.grade4)
                    GradeCriteriaRow(grade = 5, criteria = item.grade5)

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = onLogEvent,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Log AE with this Term")
                    }
                }
            }
        }
    }
}

@Composable
private fun GradeCriteriaRow(grade: Int, criteria: String) {
    val color = getGradeColor(grade)
    val label = getGradeShortLabel(grade)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(color)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "G$grade $label",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = criteria,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
    }
}

package com.pharmvigil.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pharmvigil.app.ui.components.getGradeColor
import com.pharmvigil.app.ui.components.getGradeShortLabel
import com.pharmvigil.app.viewmodel.PharmVigilViewModel

@Composable
fun AnalyticsScreen(viewModel: PharmVigilViewModel) {
    val allCases by viewModel.allCases.collectAsStateWithLifecycle()

    val totalCases = allCases.size
    val seriousCount = allCases.count { it.isSerious }
    val nonSeriousCount = totalCases - seriousCount
    val seriousPercent = if (totalCases > 0) (seriousCount.toFloat() / totalCases * 100).toInt() else 0

    // Grade breakdown
    val gradeCounts = remember(allCases) {
        val map = mutableMapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0)
        allCases.forEach { case ->
            map[case.grade] = (map[case.grade] ?: 0) + 1
        }
        map
    }

    val maxGradeCount = gradeCounts.values.maxOrNull()?.coerceAtLeast(1) ?: 1

    // SOC breakdown
    val socCounts = remember(allCases) {
        allCases.groupingBy { it.systemOrganClass }.eachCount()
            .toList()
            .sortedByDescending { it.second }
            .take(5)
    }

    // Critical cases (G4 / G5)
    val criticalCases = remember(allCases) {
        allCases.filter { it.grade >= 4 }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Header
        Text(
            text = "Pharmacovigilance Analytics",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        // Summary KPI cards
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            KpiCard(title = "Total Events", value = totalCases.toString(), subtitle = "Active trial cases", modifier = Modifier.weight(1f))
            KpiCard(title = "Serious (SAE)", value = "$seriousCount ($seriousPercent%)", subtitle = "ICH E2A criteria met", modifier = Modifier.weight(1f), highlightColor = Color(0xFFE53935))
        }

        // CTCAE Severity Frequency Chart Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Severity Distribution (CTCAE Scale)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Event frequencies stratified by Grade 1 (Mild) through Grade 5 (Death)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Bar Chart Container
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    for (g in 1..5) {
                        val count = gradeCounts[g] ?: 0
                        val targetHeight = if (totalCases == 0) 0.05f else (count.toFloat() / maxGradeCount.toFloat()).coerceAtLeast(0.05f)
                        val animatedHeight by animateFloatAsState(targetValue = targetHeight, label = "bar_height")
                        val barColor = getGradeColor(g)
                        val label = getGradeShortLabel(g)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Text(
                                text = count.toString(),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .width(36.dp)
                                    .fillMaxHeight(animatedHeight)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(barColor)
                            )
                            Text(
                                text = "G$g",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = barColor,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                text = label.take(4),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Seriousness Proportion Progress Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Seriousness Proportion",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("SAE: $seriousCount ($seriousPercent%)", style = MaterialTheme.typography.bodySmall, color = Color(0xFFFFB4AB))
                    Text("Non-serious: $nonSeriousCount (${100 - seriousPercent}%)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                LinearProgressIndicator(
                    progress = { if (totalCases > 0) seriousCount.toFloat() / totalCases else 0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = Color(0xFFE53935),
                    trackColor = MaterialTheme.colorScheme.background
                )
            }
        }

        // Top System Organ Classes (SOCs)
        if (socCounts.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Most Frequent System Organ Classes (SOC)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    socCounts.forEach { (soc, count) ->
                        val socRatio = if (totalCases > 0) count.toFloat() / totalCases else 0f
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(soc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
                                Text("$count cases", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            LinearProgressIndicator(
                                progress = { socRatio },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                }
            }
        }

        // Critical Event Sentinel Watch
        if (criticalCases.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE53935).copy(alpha = 0.15f)),
                border = BorderStroke(1.dp, Color(0xFFE53935)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFE53935), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Critical Sentinel Alerts (${criticalCases.size})",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFFFDAD6)
                        )
                    }
                    Text(
                        "Adverse events graded Grade 4 (Life-threatening) or Grade 5 (Death) requiring immediate expedited regulatory audit:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    criticalCases.forEach { critical ->
                        Text(
                            "• [G${critical.grade}] ${critical.patientId}: ${critical.adverseEventTerm} (${critical.suspectDrug}) - Onset: ${critical.onsetDate}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = Color(0xFFFFB4AB)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    highlightColor: Color = Color.Unspecified
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = if (highlightColor != Color.Unspecified) highlightColor else MaterialTheme.colorScheme.onBackground
            )
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
        }
    }
}

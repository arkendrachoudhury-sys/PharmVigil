package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AeReportingBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (String, Int) -> Unit
) {
    var aeTerm by remember { mutableStateOf("") }
    var selectedGrade by remember { mutableIntStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Report Adverse Event",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            OutlinedTextField(
                value = aeTerm,
                onValueChange = { aeTerm = it },
                label = { Text("Adverse Event Term") },
                placeholder = { Text("e.g., Nausea, Pneumonitis") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Text(
                "CTCAE Severity Grade",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SeverityGradingSelector(
                selectedGrade = selectedGrade,
                onGradeSelected = { selectedGrade = it }
            )

            Button(
                onClick = { 
                    if (aeTerm.isNotBlank() && selectedGrade > 0) {
                        onSubmit(aeTerm, selectedGrade)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = aeTerm.isNotBlank() && selectedGrade > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Submit AE Report")
            }
        }
    }
}

@Composable
fun SeverityGradingSelector(
    selectedGrade: Int,
    onGradeSelected: (Int) -> Unit
) {
    val grades = listOf(
        GradeInfo(1, "Mild", Color(0xFF4CAF50)),
        GradeInfo(2, "Moderate", Color(0xFFFFB300)),
        GradeInfo(3, "Severe", Color(0xFFFB8C00)),
        GradeInfo(4, "Life-threatening", Color(0xFFE53935)),
        GradeInfo(5, "Death", Color(0xFFB71C1C))
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        grades.forEach { grade ->
            GradeItem(
                gradeInfo = grade,
                isSelected = selectedGrade == grade.level,
                onClick = { onGradeSelected(grade.level) }
            )
        }
    }
}

data class GradeInfo(val level: Int, val description: String, val color: Color)

@Composable
fun GradeItem(
    gradeInfo: GradeInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) gradeInfo.color.copy(alpha = 0.2f) else Color.Transparent
    val borderColor = if (isSelected) gradeInfo.color else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(gradeInfo.color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "G${gradeInfo.level}",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Text(
                text = gradeInfo.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = gradeInfo.color,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

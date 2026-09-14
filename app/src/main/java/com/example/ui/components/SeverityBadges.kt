package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

fun getGradeColor(grade: Int): Color {
    return when (grade) {
        1 -> Color(0xFF4CAF50) // Green
        2 -> Color(0xFFFFB300) // Amber
        3 -> Color(0xFFFB8C00) // Orange
        4 -> Color(0xFFE53935) // Red
        5 -> Color(0xFFB71C1C) // Dark Crimson
        else -> Color(0xFF9E9E9E)
    }
}

fun getGradeShortLabel(grade: Int): String {
    return when (grade) {
        1 -> "Mild"
        2 -> "Moderate"
        3 -> "Severe"
        4 -> "Life-threatening"
        5 -> "Death"
        else -> "Unknown"
    }
}

@Composable
fun GradeBadge(
    grade: Int,
    showLabel: Boolean = true,
    modifier: Modifier = Modifier
) {
    val color = getGradeColor(grade)
    val label = getGradeShortLabel(grade)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.18f))
            .border(1.dp, color.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "G$grade",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
        if (showLabel) {
            Text(
                text = " $label",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = color,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun SeriousnessBadge(
    isSerious: Boolean,
    modifier: Modifier = Modifier
) {
    if (isSerious) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFE53935).copy(alpha = 0.2f))
                .border(1.dp, Color(0xFFE53935), RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Serious Event",
                tint = Color(0xFFE53935),
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = " SAE (Serious)",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFFFFB4AB),
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    } else {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "AE (Non-serious)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

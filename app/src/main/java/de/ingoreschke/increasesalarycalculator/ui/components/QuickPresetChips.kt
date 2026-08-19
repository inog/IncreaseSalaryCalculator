package de.ingoreschke.increasesalarycalculator.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

private val PRESETS = listOf(2.0, 3.0, 5.0, 7.5, 10.0, 15.0, 20.0)

@Composable
fun QuickPresetChips(
    modifier: Modifier = Modifier,
    currentPercentage: Float,
    onPresetSelected: (Double) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PRESETS.forEach { preset ->
            val isSelected = (currentPercentage - preset.toFloat()).let { diff ->
                diff in -0.05f..0.05f
            }
            val labelText = if (preset % 1.0 == 0.0) "+${preset.toInt()}%" else "+$preset%"

            FilterChip(
                selected = isSelected,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onPresetSelected(preset)
                },
                label = { Text(labelText) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

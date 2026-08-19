package de.ingoreschke.increasesalarycalculator.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.ingoreschke.increasesalarycalculator.R
import de.ingoreschke.increasesalarycalculator.data.CalculationMode
import de.ingoreschke.increasesalarycalculator.data.SalaryCalculationResult
import de.ingoreschke.increasesalarycalculator.data.SalaryPeriod
import de.ingoreschke.increasesalarycalculator.data.TargetCalculationResult
import de.ingoreschke.increasesalarycalculator.domain.SalaryCalculator
import java.math.BigDecimal

@Composable
fun SalaryResultCard(
    modifier: Modifier = Modifier,
    mode: CalculationMode,
    percentageResult: SalaryCalculationResult?,
    targetResult: TargetCalculationResult?,
    onCopyClick: () -> Unit
) {
    val ctx = LocalContext.current
    val locale = ctx.resources.configuration.locales[0]

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            when (mode) {
                CalculationMode.PERCENTAGE -> {
                    val result = percentageResult ?: return@ElevatedCard
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.label_result_salary),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        IconButton(onClick = onCopyClick) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = stringResource(id = R.string.action_copy)
                            )
                        }
                    }

                    AnimatedContent(
                        targetState = SalaryCalculator.formatCurrency(result.newSalary, locale),
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "NewSalaryAnimation"
                    ) { formattedSalary ->
                        Text(
                            text = formattedSalary,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 34.sp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val sign = if (result.difference >= BigDecimal.ZERO) "+" else ""
                        val diffText = "$sign${SalaryCalculator.formatCurrency(result.difference, locale)}"
                        
                        SuggestionChip(
                            onClick = {},
                            label = { Text(diffText, fontWeight = FontWeight.SemiBold) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )

                        val percentText = "$sign${SalaryCalculator.formatPercentage(result.increasePercentage, locale)}"
                        SuggestionChip(
                            onClick = {},
                            label = { Text(percentText) }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(14.dp))

                    DetailRow(
                        label = if (result.period == SalaryPeriod.MONTHLY) {
                            stringResource(id = R.string.label_difference_monthly)
                        } else {
                            stringResource(id = R.string.label_difference_annual)
                        },
                        value = "+${SalaryCalculator.formatCurrency(result.difference, locale)}"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DetailRow(
                        label = if (result.period == SalaryPeriod.MONTHLY) {
                            stringResource(id = R.string.label_difference_annual)
                        } else {
                            stringResource(id = R.string.label_difference_monthly)
                        },
                        value = if (result.period == SalaryPeriod.MONTHLY) {
                            "+${SalaryCalculator.formatCurrency(result.annualDifference, locale)}"
                        } else {
                            "+${SalaryCalculator.formatCurrency(result.monthlyDifference, locale)}"
                        }
                    )
                }

                CalculationMode.TARGET_SALARY -> {
                    val result = targetResult ?: return@ElevatedCard

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.label_calculated_increase),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        IconButton(onClick = onCopyClick) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = stringResource(id = R.string.action_copy)
                            )
                        }
                    }

                    val sign = if (result.requiredPercentage >= BigDecimal.ZERO) "+" else ""
                    val percentStr = "$sign${SalaryCalculator.formatPercentage(result.requiredPercentage, locale)}"

                    AnimatedContent(
                        targetState = percentStr,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "TargetPercentAnimation"
                    ) { formattedPercent ->
                        Text(
                            text = formattedPercent,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    val diffText = "$sign${SalaryCalculator.formatCurrency(result.difference, locale)}"
                    SuggestionChip(
                        onClick = {},
                        label = { Text(diffText, fontWeight = FontWeight.SemiBold) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(14.dp))

                    DetailRow(
                        label = stringResource(id = R.string.label_difference_annual),
                        value = "$sign${SalaryCalculator.formatCurrency(result.annualDifference, locale)}"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DetailRow(
                        label = stringResource(id = R.string.label_difference_monthly),
                        value = "$sign${SalaryCalculator.formatCurrency(result.monthlyDifference, locale)}"
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

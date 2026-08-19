package de.ingoreschke.increasesalarycalculator.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.ingoreschke.increasesalarycalculator.R
import de.ingoreschke.increasesalarycalculator.data.CalculationMode
import de.ingoreschke.increasesalarycalculator.data.SalaryPeriod
import de.ingoreschke.increasesalarycalculator.domain.SalaryCalculator
import de.ingoreschke.increasesalarycalculator.ui.components.AdMobAdaptiveBanner
import de.ingoreschke.increasesalarycalculator.ui.components.NegotiationTipsDialog
import de.ingoreschke.increasesalarycalculator.ui.components.QuickPresetChips
import de.ingoreschke.increasesalarycalculator.ui.components.SalaryResultCard
import de.ingoreschke.increasesalarycalculator.ui.components.SalaryTopAppBar
import kotlinx.coroutines.launch
import java.util.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryScreen(
    viewModel: SalaryViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val locale = context.resources.configuration.locales[0]

    val currencySymbol = remember(locale) {
        try {
            Currency.getInstance(locale).symbol
        } catch (_: Exception) {
            "€"
        }
    }

    val copyResult = {
        val shareText = when (state.calculationMode) {
            CalculationMode.PERCENTAGE -> {
                val res = state.percentageResult
                if (res != null) {
                    context.getString(
                        R.string.share_text,
                        SalaryCalculator.formatCurrency(res.baseSalary, locale),
                        if (res.period == SalaryPeriod.MONTHLY) context.getString(R.string.period_monthly) else context.getString(R.string.period_annual),
                        SalaryCalculator.formatPercentage(res.increasePercentage, locale),
                        SalaryCalculator.formatCurrency(res.newSalary, locale),
                        SalaryCalculator.formatCurrency(res.difference, locale),
                        SalaryCalculator.formatCurrency(res.annualDifference, locale)
                    )
                } else ""
            }
            CalculationMode.TARGET_SALARY -> {
                val res = state.targetResult
                if (res != null) {
                    context.getString(
                        R.string.share_text,
                        SalaryCalculator.formatCurrency(res.baseSalary, locale),
                        if (res.period == SalaryPeriod.MONTHLY) context.getString(R.string.period_monthly) else context.getString(R.string.period_annual),
                        SalaryCalculator.formatPercentage(res.requiredPercentage, locale),
                        SalaryCalculator.formatCurrency(res.targetSalary, locale),
                        SalaryCalculator.formatCurrency(res.difference, locale),
                        SalaryCalculator.formatCurrency(res.annualDifference, locale)
                    )
                } else ""
            }
        }

        if (shareText.isNotBlank()) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Salary Increase", shareText)
            clipboard.setPrimaryClip(clip)
            scope.launch {
                snackbarHostState.showSnackbar(context.getString(R.string.copied_to_clipboard))
            }
        }
    }

    val shareResult = {
        val shareText = when (state.calculationMode) {
            CalculationMode.PERCENTAGE -> {
                val res = state.percentageResult
                if (res != null) {
                    context.getString(
                        R.string.share_text,
                        SalaryCalculator.formatCurrency(res.baseSalary, locale),
                        if (res.period == SalaryPeriod.MONTHLY) context.getString(R.string.period_monthly) else context.getString(R.string.period_annual),
                        SalaryCalculator.formatPercentage(res.increasePercentage, locale),
                        SalaryCalculator.formatCurrency(res.newSalary, locale),
                        SalaryCalculator.formatCurrency(res.difference, locale),
                        SalaryCalculator.formatCurrency(res.annualDifference, locale)
                    )
                } else ""
            }
            CalculationMode.TARGET_SALARY -> {
                val res = state.targetResult
                if (res != null) {
                    context.getString(
                        R.string.share_text,
                        SalaryCalculator.formatCurrency(res.baseSalary, locale),
                        if (res.period == SalaryPeriod.MONTHLY) context.getString(R.string.period_monthly) else context.getString(R.string.period_annual),
                        SalaryCalculator.formatPercentage(res.requiredPercentage, locale),
                        SalaryCalculator.formatCurrency(res.targetSalary, locale),
                        SalaryCalculator.formatCurrency(res.difference, locale),
                        SalaryCalculator.formatCurrency(res.annualDifference, locale)
                    )
                } else ""
            }
        }

        if (shareText.isNotBlank()) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_title))
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.action_share)))
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        topBar = {
            SalaryTopAppBar(
                onInfoClick = { viewModel.toggleInfoDialog(true) },
                onResetClick = { viewModel.onReset() },
                onShareClick = { shareResult() }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(scrollState)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Mode Switch (Prozentual vs Zielgehalt)
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val modes = listOf(
                        CalculationMode.PERCENTAGE to stringResource(id = R.string.tab_standard),
                        CalculationMode.TARGET_SALARY to stringResource(id = R.string.tab_target)
                    )
                    modes.forEachIndexed { index, (mode, label) ->
                        SegmentedButton(
                            selected = state.calculationMode == mode,
                            onClick = { viewModel.onModeChanged(mode) },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = modes.size)
                        ) {
                            Text(label, fontWeight = if (state.calculationMode == mode) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }

                // Period Switch (Monatlich vs Jährlich)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = state.selectedPeriod == SalaryPeriod.MONTHLY,
                        onClick = { viewModel.onPeriodChanged(SalaryPeriod.MONTHLY) },
                        label = { Text(stringResource(id = R.string.period_monthly)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    FilterChip(
                        selected = state.selectedPeriod == SalaryPeriod.ANNUAL,
                        onClick = { viewModel.onPeriodChanged(SalaryPeriod.ANNUAL) },
                        label = { Text(stringResource(id = R.string.period_annual)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }

                // Current Salary Input
                OutlinedTextField(
                    value = state.salaryInput,
                    onValueChange = { viewModel.onSalaryChanged(it) },
                    label = { Text(stringResource(id = R.string.label_current_salary)) },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (state.salaryInput.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSalaryChanged("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = stringResource(id = R.string.clear_input)
                                    )
                                }
                            }
                            Text(currencySymbol, modifier = Modifier.padding(end = 12.dp))
                        }
                    },
                    isError = !state.isSalaryValid,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = FocusDirection.Down.let { ImeAction.Next }
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (state.calculationMode == CalculationMode.PERCENTAGE) {
                    // Percentage Input
                    OutlinedTextField(
                        value = state.percentageInput,
                        onValueChange = { viewModel.onPercentageChanged(it) },
                        label = { Text(stringResource(id = R.string.label_increase_percentage)) },
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (state.percentageInput.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onPercentageChanged("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = stringResource(id = R.string.clear_input)
                                        )
                                    }
                                }
                                Text("%", modifier = Modifier.padding(end = 12.dp))
                            }
                        },
                        isError = !state.isPercentageValid,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Quick Preset Chips
                    QuickPresetChips(
                        currentPercentage = state.sliderPercentage,
                        onPresetSelected = { viewModel.onPresetSelected(it) }
                    )

                    // Percentage Slider (0 to 50%)
                    Column {
                        Slider(
                            value = state.sliderPercentage,
                            valueRange = 0f..50f,
                            onValueChange = { viewModel.onSliderPercentageChanged(it) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("0 %", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("25 %", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("50 %", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                } else {
                    // Target Salary Input
                    OutlinedTextField(
                        value = state.targetSalaryInput,
                        onValueChange = { viewModel.onTargetSalaryChanged(it) },
                        label = { Text(stringResource(id = R.string.label_target_salary)) },
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (state.targetSalaryInput.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onTargetSalaryChanged("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = stringResource(id = R.string.clear_input)
                                        )
                                    }
                                }
                                Text(currencySymbol, modifier = Modifier.padding(end = 12.dp))
                            }
                        },
                        isError = !state.isTargetSalaryValid,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Result Card
                SalaryResultCard(
                    mode = state.calculationMode,
                    percentageResult = state.percentageResult,
                    targetResult = state.targetResult,
                    onCopyClick = { copyResult() }
                )

                Text(
                    text = stringResource(id = R.string.label_gross_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Bottom AdMob Banner
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp)
            ) {
                AdMobAdaptiveBanner()
            }
        }

        // Negotiation Tips Dialog
        if (state.showInfoDialog) {
            NegotiationTipsDialog(
                onDismissRequest = { viewModel.toggleInfoDialog(false) }
            )
        }
    }
}

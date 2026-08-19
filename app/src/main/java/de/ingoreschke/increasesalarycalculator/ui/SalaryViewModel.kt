package de.ingoreschke.increasesalarycalculator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import de.ingoreschke.increasesalarycalculator.data.CalculationMode
import de.ingoreschke.increasesalarycalculator.data.CurrencyOption
import de.ingoreschke.increasesalarycalculator.data.SalaryPeriod
import de.ingoreschke.increasesalarycalculator.data.SalaryPreferencesRepository
import de.ingoreschke.increasesalarycalculator.domain.SalaryCalculator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

class SalaryViewModel(
    private val repository: SalaryPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalaryUiState())
    val uiState: StateFlow<SalaryUiState> = _uiState.asStateFlow()

    private var saveJob: Job? = null

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            val prefs = repository.userPreferencesFlow.first()
            val salary = prefs.lastSalary
            val percentage = prefs.lastPercentage
            val targetSalary = prefs.lastTargetSalary
            val period = prefs.period
            val mode = prefs.mode
            val currency = prefs.currencyCode

            val parsedSalary = SalaryCalculator.parseToBigDecimal(salary) ?: BigDecimal("3500.00")
            val parsedPercentage = SalaryCalculator.parseToBigDecimal(percentage) ?: BigDecimal("5.0")
            val parsedTargetSalary = SalaryCalculator.parseToBigDecimal(targetSalary) ?: BigDecimal("3850.00")

            val percentageResult = SalaryCalculator.calculateIncrease(
                baseSalary = parsedSalary,
                percentage = parsedPercentage,
                period = period
            )

            val targetResult = SalaryCalculator.calculateRequiredPercentage(
                baseSalary = parsedSalary,
                targetSalary = parsedTargetSalary,
                period = period
            )

            _uiState.update {
                it.copy(
                    salaryInput = salary,
                    percentageInput = percentage,
                    targetSalaryInput = targetSalary,
                    sliderPercentage = parsedPercentage.toFloat().coerceIn(0f, 50f),
                    selectedPeriod = period,
                    calculationMode = mode,
                    selectedCurrencyCode = currency,
                    percentageResult = percentageResult,
                    targetResult = targetResult,
                    isLoaded = true
                )
            }
        }
    }

    fun onSalaryChanged(input: String) {
        val parsedSalary = SalaryCalculator.parseToBigDecimal(input)
        val isInputValid = input.isEmpty() || parsedSalary != null

        _uiState.update { current ->
            val salary = parsedSalary ?: BigDecimal.ZERO
            val percentage = SalaryCalculator.parseToBigDecimal(current.percentageInput) ?: BigDecimal.ZERO
            val target = SalaryCalculator.parseToBigDecimal(current.targetSalaryInput) ?: BigDecimal.ZERO

            val newPercentResult = SalaryCalculator.calculateIncrease(
                baseSalary = salary,
                percentage = percentage,
                period = current.selectedPeriod
            )
            val newTargetResult = SalaryCalculator.calculateRequiredPercentage(
                baseSalary = salary,
                targetSalary = target,
                period = current.selectedPeriod
            )

            current.copy(
                salaryInput = input,
                isSalaryValid = isInputValid,
                percentageResult = newPercentResult,
                targetResult = newTargetResult
            )
        }
        debouncedSave()
    }

    fun onPercentageChanged(input: String) {
        val parsedPercentage = SalaryCalculator.parseToBigDecimal(input)
        val isInputValid = input.isEmpty() || parsedPercentage != null

        _uiState.update { current ->
            val percentage = parsedPercentage ?: BigDecimal.ZERO
            val salary = SalaryCalculator.parseToBigDecimal(current.salaryInput) ?: BigDecimal.ZERO
            val sliderVal = parsedPercentage?.toFloat()?.coerceIn(0f, 50f) ?: current.sliderPercentage

            val newPercentResult = SalaryCalculator.calculateIncrease(
                baseSalary = salary,
                percentage = percentage,
                period = current.selectedPeriod
            )

            current.copy(
                percentageInput = input,
                sliderPercentage = sliderVal,
                isPercentageValid = isInputValid,
                percentageResult = newPercentResult
            )
        }
        debouncedSave()
    }

    fun onSliderPercentageChanged(value: Float) {
        val rounded = ((value * 10f).roundToInt() / 10f)
        val percentageStr = if (rounded % 1.0f == 0.0f) rounded.toInt().toString() else rounded.toString()
        val parsedPercentage = BigDecimal(rounded.toString()).setScale(2, RoundingMode.HALF_UP)

        _uiState.update { current ->
            val salary = SalaryCalculator.parseToBigDecimal(current.salaryInput) ?: BigDecimal.ZERO
            val newPercentResult = SalaryCalculator.calculateIncrease(
                baseSalary = salary,
                percentage = parsedPercentage,
                period = current.selectedPeriod
            )

            current.copy(
                percentageInput = percentageStr,
                sliderPercentage = rounded,
                isPercentageValid = true,
                percentageResult = newPercentResult
            )
        }
        debouncedSave()
    }

    fun onTargetSalaryChanged(input: String) {
        val parsedTarget = SalaryCalculator.parseToBigDecimal(input)
        val isInputValid = input.isEmpty() || parsedTarget != null

        _uiState.update { current ->
            val target = parsedTarget ?: BigDecimal.ZERO
            val salary = SalaryCalculator.parseToBigDecimal(current.salaryInput) ?: BigDecimal.ZERO

            val newTargetResult = SalaryCalculator.calculateRequiredPercentage(
                baseSalary = salary,
                targetSalary = target,
                period = current.selectedPeriod
            )

            current.copy(
                targetSalaryInput = input,
                isTargetSalaryValid = isInputValid,
                targetResult = newTargetResult
            )
        }
        debouncedSave()
    }

    fun onPresetSelected(preset: Double) {
        val presetStr = if (preset % 1.0 == 0.0) preset.toInt().toString() else preset.toString()
        onPercentageChanged(presetStr)
    }

    fun onPeriodChanged(period: SalaryPeriod) {
        _uiState.update { current ->
            val salary = SalaryCalculator.parseToBigDecimal(current.salaryInput) ?: BigDecimal.ZERO
            val percentage = SalaryCalculator.parseToBigDecimal(current.percentageInput) ?: BigDecimal.ZERO
            val target = SalaryCalculator.parseToBigDecimal(current.targetSalaryInput) ?: BigDecimal.ZERO

            val newPercentResult = SalaryCalculator.calculateIncrease(
                baseSalary = salary,
                percentage = percentage,
                period = period
            )
            val newTargetResult = SalaryCalculator.calculateRequiredPercentage(
                baseSalary = salary,
                targetSalary = target,
                period = period
            )

            current.copy(
                selectedPeriod = period,
                percentageResult = newPercentResult,
                targetResult = newTargetResult
            )
        }
        viewModelScope.launch {
            repository.updatePeriod(period)
        }
    }

    fun onModeChanged(mode: CalculationMode) {
        _uiState.update { it.copy(calculationMode = mode) }
        viewModelScope.launch {
            repository.updateMode(mode)
        }
    }

    fun onCurrencySelected(currencyCode: String) {
        _uiState.update {
            it.copy(
                selectedCurrencyCode = currencyCode,
                showCurrencyDialog = false
            )
        }
        viewModelScope.launch {
            repository.updateCurrency(currencyCode)
        }
    }

    fun onReset() {
        _uiState.update { current ->
            current.copy(
                salaryInput = "",
                percentageInput = "",
                targetSalaryInput = "",
                sliderPercentage = 0f,
                percentageResult = null,
                targetResult = null,
                isSalaryValid = true,
                isPercentageValid = true,
                isTargetSalaryValid = true
            )
        }
        viewModelScope.launch {
            repository.reset()
        }
    }

    fun toggleInfoDialog(show: Boolean) {
        _uiState.update { it.copy(showInfoDialog = show) }
    }

    fun toggleCurrencyDialog(show: Boolean) {
        _uiState.update { it.copy(showCurrencyDialog = show) }
    }

    private fun debouncedSave() {
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            delay(400)
            val state = _uiState.value
            if (state.salaryInput.isNotBlank()) repository.updateSalary(state.salaryInput)
            if (state.percentageInput.isNotBlank()) repository.updatePercentage(state.percentageInput)
            if (state.targetSalaryInput.isNotBlank()) repository.updateTargetSalary(state.targetSalaryInput)
        }
    }

    companion object {
        fun provideFactory(repository: SalaryPreferencesRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SalaryViewModel(repository) as T
                }
            }
    }
}

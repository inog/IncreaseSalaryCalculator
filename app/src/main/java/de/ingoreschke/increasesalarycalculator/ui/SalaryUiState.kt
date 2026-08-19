package de.ingoreschke.increasesalarycalculator.ui

import de.ingoreschke.increasesalarycalculator.data.CalculationMode
import de.ingoreschke.increasesalarycalculator.data.CurrencyOption
import de.ingoreschke.increasesalarycalculator.data.SalaryCalculationResult
import de.ingoreschke.increasesalarycalculator.data.SalaryPeriod
import de.ingoreschke.increasesalarycalculator.data.TargetCalculationResult

data class SalaryUiState(
    val salaryInput: String = "",
    val percentageInput: String = "",
    val targetSalaryInput: String = "",
    val sliderPercentage: Float = 0f,
    val selectedPeriod: SalaryPeriod = SalaryPeriod.MONTHLY,
    val calculationMode: CalculationMode = CalculationMode.PERCENTAGE,
    val selectedCurrencyCode: String = CurrencyOption.CODE_AUTO,
    val percentageResult: SalaryCalculationResult? = null,
    val targetResult: TargetCalculationResult? = null,
    val isSalaryValid: Boolean = true,
    val isPercentageValid: Boolean = true,
    val isTargetSalaryValid: Boolean = true,
    val showInfoDialog: Boolean = false,
    val showCurrencyDialog: Boolean = false,
    val isLoaded: Boolean = false
)

package de.ingoreschke.increasesalarycalculator.data

import java.math.BigDecimal

/**
 * Encapsulates the results of a target salary calculation (calculating required percentage).
 */
data class TargetCalculationResult(
    val baseSalary: BigDecimal,
    val targetSalary: BigDecimal,
    val period: SalaryPeriod,
    val requiredPercentage: BigDecimal,
    val difference: BigDecimal,
    val monthlyDifference: BigDecimal,
    val annualDifference: BigDecimal
)

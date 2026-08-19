package de.ingoreschke.increasesalarycalculator.data

import java.math.BigDecimal

/**
 * Encapsulates the results of a percentage-based salary increase calculation.
 */
data class SalaryCalculationResult(
    val baseSalary: BigDecimal,
    val increasePercentage: BigDecimal,
    val period: SalaryPeriod,
    val newSalary: BigDecimal,
    val difference: BigDecimal,
    val monthlyBaseSalary: BigDecimal,
    val monthlyNewSalary: BigDecimal,
    val monthlyDifference: BigDecimal,
    val annualBaseSalary: BigDecimal,
    val annualNewSalary: BigDecimal,
    val annualDifference: BigDecimal
)

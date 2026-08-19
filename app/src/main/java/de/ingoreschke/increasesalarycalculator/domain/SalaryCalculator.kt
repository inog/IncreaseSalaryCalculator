package de.ingoreschke.increasesalarycalculator.domain

import de.ingoreschke.increasesalarycalculator.data.CurrencyOption
import de.ingoreschke.increasesalarycalculator.data.SalaryCalculationResult
import de.ingoreschke.increasesalarycalculator.data.SalaryPeriod
import de.ingoreschke.increasesalarycalculator.data.TargetCalculationResult
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object SalaryCalculator {

    private val HUNDRED = BigDecimal("100")
    private val TWELVE = BigDecimal("12")

    private data class Breakdown(
        val monthlyBase: BigDecimal,
        val monthlyNew: BigDecimal,
        val monthlyDiff: BigDecimal,
        val annualBase: BigDecimal,
        val annualNew: BigDecimal,
        val annualDiff: BigDecimal
    )

    /**
     * Calculates the new salary and financial difference for a percentage increase.
     */
    fun calculateIncrease(
        baseSalary: BigDecimal,
        percentage: BigDecimal,
        period: SalaryPeriod = SalaryPeriod.MONTHLY
    ): SalaryCalculationResult {
        val safeBase = if (baseSalary < BigDecimal.ZERO) BigDecimal.ZERO else baseSalary
        val safePercentage = percentage

        val difference = safeBase.multiply(safePercentage)
            .divide(HUNDRED, 4, RoundingMode.HALF_UP)
            .setScale(2, RoundingMode.HALF_UP)

        val newSalary = safeBase.add(difference).setScale(2, RoundingMode.HALF_UP)

        val breakdown = when (period) {
            SalaryPeriod.MONTHLY -> {
                val mBase = safeBase.setScale(2, RoundingMode.HALF_UP)
                val mNew = newSalary
                val mDiff = difference
                val aBase = mBase.multiply(TWELVE).setScale(2, RoundingMode.HALF_UP)
                val aNew = mNew.multiply(TWELVE).setScale(2, RoundingMode.HALF_UP)
                val aDiff = mDiff.multiply(TWELVE).setScale(2, RoundingMode.HALF_UP)
                Breakdown(mBase, mNew, mDiff, aBase, aNew, aDiff)
            }
            SalaryPeriod.ANNUAL -> {
                val aBase = safeBase.setScale(2, RoundingMode.HALF_UP)
                val aNew = newSalary
                val aDiff = difference
                val mBase = aBase.divide(TWELVE, 2, RoundingMode.HALF_UP)
                val mNew = aNew.divide(TWELVE, 2, RoundingMode.HALF_UP)
                val mDiff = aDiff.divide(TWELVE, 2, RoundingMode.HALF_UP)
                Breakdown(mBase, mNew, mDiff, aBase, aNew, aDiff)
            }
        }

        return SalaryCalculationResult(
            baseSalary = safeBase.setScale(2, RoundingMode.HALF_UP),
            increasePercentage = safePercentage.setScale(2, RoundingMode.HALF_UP),
            period = period,
            newSalary = newSalary,
            difference = difference,
            monthlyBaseSalary = breakdown.monthlyBase,
            monthlyNewSalary = breakdown.monthlyNew,
            monthlyDifference = breakdown.monthlyDiff,
            annualBaseSalary = breakdown.annualBase,
            annualNewSalary = breakdown.annualNew,
            annualDifference = breakdown.annualDiff
        )
    }

    /**
     * Calculates the required percentage increase to reach a desired target salary.
     */
    fun calculateRequiredPercentage(
        baseSalary: BigDecimal,
        targetSalary: BigDecimal,
        period: SalaryPeriod = SalaryPeriod.MONTHLY
    ): TargetCalculationResult {
        val safeBase = if (baseSalary < BigDecimal.ZERO) BigDecimal.ZERO else baseSalary
        val safeTarget = if (targetSalary < BigDecimal.ZERO) BigDecimal.ZERO else targetSalary

        val difference = safeTarget.subtract(safeBase).setScale(2, RoundingMode.HALF_UP)

        val requiredPercentage = if (safeBase.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
        } else {
            difference.multiply(HUNDRED)
                .divide(safeBase, 4, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP)
        }

        val (monthlyDiff, annualDiff) = when (period) {
            SalaryPeriod.MONTHLY -> {
                val mDiff = difference
                val aDiff = difference.multiply(TWELVE).setScale(2, RoundingMode.HALF_UP)
                Pair(mDiff, aDiff)
            }
            SalaryPeriod.ANNUAL -> {
                val aDiff = difference
                val mDiff = difference.divide(TWELVE, 2, RoundingMode.HALF_UP)
                Pair(mDiff, aDiff)
            }
        }

        return TargetCalculationResult(
            baseSalary = safeBase.setScale(2, RoundingMode.HALF_UP),
            targetSalary = safeTarget.setScale(2, RoundingMode.HALF_UP),
            period = period,
            requiredPercentage = requiredPercentage,
            difference = difference,
            monthlyDifference = monthlyDiff,
            annualDifference = annualDiff
        )
    }

    /**
     * Safely parses a user-entered number string (supporting both '.' and ',' decimal separators).
     */
    fun parseToBigDecimal(input: String?): BigDecimal? {
        if (input.isNullOrBlank()) return null
        val normalized = input.trim()
            .replace(" ", "")
            .replace("'", "")
            .replace(",", ".")

        // Support single trailing dot like "12."
        val parseable = if (normalized.endsWith(".")) normalized.dropLast(1) else normalized
        if (parseable.isEmpty()) return null

        return try {
            BigDecimal(parseable)
        } catch (_: NumberFormatException) {
            null
        }
    }

    /**
     * Formats a BigDecimal to localized currency string according to selected CurrencyOption.
     */
    fun formatCurrency(
        amount: BigDecimal,
        currencyCode: String = CurrencyOption.CODE_AUTO,
        deviceLocale: Locale = Locale.getDefault()
    ): String {
        return try {
            val option = CurrencyOption.getByCode(currencyCode, deviceLocale)
            val formatLocale = option.locale ?: deviceLocale
            val format = NumberFormat.getCurrencyInstance(formatLocale)
            if (option.code != CurrencyOption.CODE_AUTO) {
                try {
                    format.currency = Currency.getInstance(option.code)
                } catch (_: Exception) {}
            }
            format.format(amount)
        } catch (_: Exception) {
            val format = NumberFormat.getCurrencyInstance(Locale.GERMANY)
            format.format(amount)
        }
    }

    /**
     * Gets the display symbol for a selected currency code.
     */
    fun getCurrencySymbol(
        currencyCode: String = CurrencyOption.CODE_AUTO,
        deviceLocale: Locale = Locale.getDefault()
    ): String {
        return CurrencyOption.getByCode(currencyCode, deviceLocale).symbol
    }

    /**
     * Formats a percentage value (e.g. 5.50 -> "5.5%" or "5,5 %").
     */
    fun formatPercentage(percentage: BigDecimal, locale: Locale = Locale.getDefault()): String {
        val numberFormat = NumberFormat.getNumberInstance(locale).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 2
        }
        return "${numberFormat.format(percentage)} %"
    }
}

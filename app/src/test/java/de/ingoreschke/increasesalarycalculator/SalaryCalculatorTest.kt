package de.ingoreschke.increasesalarycalculator

import de.ingoreschke.increasesalarycalculator.data.CurrencyOption
import de.ingoreschke.increasesalarycalculator.data.SalaryPeriod
import de.ingoreschke.increasesalarycalculator.domain.SalaryCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.util.Locale

class SalaryCalculatorTest {

    @Test
    fun calculateIncrease_standardMonthlyValues_calculatesCorrectly() {
        val base = BigDecimal("3500.00")
        val percentage = BigDecimal("5.00")
        val result = SalaryCalculator.calculateIncrease(base, percentage, SalaryPeriod.MONTHLY)

        assertEquals(BigDecimal("3675.00"), result.newSalary)
        assertEquals(BigDecimal("175.00"), result.difference)
        assertEquals(BigDecimal("3500.00"), result.monthlyBaseSalary)
        assertEquals(BigDecimal("3675.00"), result.monthlyNewSalary)
        assertEquals(BigDecimal("175.00"), result.monthlyDifference)
        assertEquals(BigDecimal("42000.00"), result.annualBaseSalary)
        assertEquals(BigDecimal("44100.00"), result.annualNewSalary)
        assertEquals(BigDecimal("2100.00"), result.annualDifference)
    }

    @Test
    fun calculateIncrease_annualValues_calculatesCorrectly() {
        val base = BigDecimal("48000.00")
        val percentage = BigDecimal("10.00")
        val result = SalaryCalculator.calculateIncrease(base, percentage, SalaryPeriod.ANNUAL)

        assertEquals(BigDecimal("52800.00"), result.newSalary)
        assertEquals(BigDecimal("4800.00"), result.difference)
        assertEquals(BigDecimal("4000.00"), result.monthlyBaseSalary)
        assertEquals(BigDecimal("4400.00"), result.monthlyNewSalary)
        assertEquals(BigDecimal("400.00"), result.monthlyDifference)
        assertEquals(BigDecimal("48000.00"), result.annualBaseSalary)
        assertEquals(BigDecimal("52800.00"), result.annualNewSalary)
        assertEquals(BigDecimal("4800.00"), result.annualDifference)
    }

    @Test
    fun calculateIncrease_zeroPercentage_returnsSameSalary() {
        val base = BigDecimal("4000.00")
        val percentage = BigDecimal.ZERO
        val result = SalaryCalculator.calculateIncrease(base, percentage, SalaryPeriod.MONTHLY)

        assertEquals(BigDecimal("4000.00"), result.newSalary)
        assertEquals(BigDecimal("0.00"), result.difference)
    }

    @Test
    fun calculateIncrease_roundingHalfUp_roundsCorrectly() {
        val base = BigDecimal("1234.56")
        val percentage = BigDecimal("3.33") // 1234.56 * 0.0333 = 41.110848 -> 41.11
        val result = SalaryCalculator.calculateIncrease(base, percentage, SalaryPeriod.MONTHLY)

        assertEquals(BigDecimal("41.11"), result.difference)
        assertEquals(BigDecimal("1275.67"), result.newSalary)
    }

    @Test
    fun calculateIncrease_negativeBaseSalary_clampedToZero() {
        val base = BigDecimal("-500.00")
        val percentage = BigDecimal("10.00")
        val result = SalaryCalculator.calculateIncrease(base, percentage, SalaryPeriod.MONTHLY)

        assertEquals(BigDecimal("0.00"), result.newSalary)
        assertEquals(BigDecimal("0.00"), result.difference)
    }

    @Test
    fun calculateRequiredPercentage_standardValues_calculatesCorrectly() {
        val base = BigDecimal("4000.00")
        val target = BigDecimal("4400.00")
        val result = SalaryCalculator.calculateRequiredPercentage(base, target, SalaryPeriod.MONTHLY)

        assertEquals(BigDecimal("10.00"), result.requiredPercentage)
        assertEquals(BigDecimal("400.00"), result.difference)
        assertEquals(BigDecimal("400.00"), result.monthlyDifference)
        assertEquals(BigDecimal("4800.00"), result.annualDifference)
    }

    @Test
    fun calculateRequiredPercentage_zeroBaseSalary_returnsZeroPercentage() {
        val base = BigDecimal.ZERO
        val target = BigDecimal("3000.00")
        val result = SalaryCalculator.calculateRequiredPercentage(base, target, SalaryPeriod.MONTHLY)

        assertEquals(BigDecimal("0.00"), result.requiredPercentage)
    }

    @Test
    fun parseToBigDecimal_validInputs_parsesAccurately() {
        assertEquals(BigDecimal("3500"), SalaryCalculator.parseToBigDecimal("3500"))
        assertEquals(BigDecimal("3500.5"), SalaryCalculator.parseToBigDecimal("3500.5"))
        assertEquals(BigDecimal("3500.5"), SalaryCalculator.parseToBigDecimal("3500,5"))
        assertEquals(BigDecimal("3500"), SalaryCalculator.parseToBigDecimal("3500."))
        assertEquals(BigDecimal("3500"), SalaryCalculator.parseToBigDecimal("3500,"))
        assertEquals(BigDecimal("3500.75"), SalaryCalculator.parseToBigDecimal(" 3 500,75 "))
        assertEquals(BigDecimal("12500.00"), SalaryCalculator.parseToBigDecimal("12'500.00"))
    }

    @Test
    fun parseToBigDecimal_invalidInputs_returnsNull() {
        assertNull(SalaryCalculator.parseToBigDecimal(null))
        assertNull(SalaryCalculator.parseToBigDecimal(""))
        assertNull(SalaryCalculator.parseToBigDecimal("   "))
        assertNull(SalaryCalculator.parseToBigDecimal("abc"))
        assertNull(SalaryCalculator.parseToBigDecimal("12.34.56"))
    }

    @Test
    fun formatCurrency_withCustomCurrencies_formatsAppropriately() {
        val amount = BigDecimal("1234.50")
        
        val eurFormat = SalaryCalculator.formatCurrency(amount, CurrencyOption.CODE_EUR, Locale.GERMANY)
        assertTrue(eurFormat.contains("1.234,50") || eurFormat.contains("1234,50"))
        assertTrue(eurFormat.contains("€"))

        val usdFormat = SalaryCalculator.formatCurrency(amount, CurrencyOption.CODE_USD, Locale.US)
        assertTrue(usdFormat.contains("1,234.50") || usdFormat.contains("1234.50"))
        assertTrue(usdFormat.contains("$"))

        val gbpFormat = SalaryCalculator.formatCurrency(amount, CurrencyOption.CODE_GBP, Locale.UK)
        assertTrue(gbpFormat.contains("£"))

        val chfFormat = SalaryCalculator.formatCurrency(amount, CurrencyOption.CODE_CHF, Locale.GERMANY)
        assertTrue(chfFormat.contains("CHF"))
    }

    @Test
    fun getCurrencySymbol_returnsExpectedSymbols() {
        assertEquals("€", SalaryCalculator.getCurrencySymbol(CurrencyOption.CODE_EUR, Locale.GERMANY))
        assertEquals("$", SalaryCalculator.getCurrencySymbol(CurrencyOption.CODE_USD, Locale.US))
        assertEquals("£", SalaryCalculator.getCurrencySymbol(CurrencyOption.CODE_GBP, Locale.UK))
        assertEquals("CHF", SalaryCalculator.getCurrencySymbol(CurrencyOption.CODE_CHF, Locale.GERMANY))
    }

    @Test
    fun formatPercentage_formatsCorrectly() {
        val formatted = SalaryCalculator.formatPercentage(BigDecimal("7.50"), Locale.GERMANY)
        assertTrue(formatted.contains("7,5") || formatted.contains("7.5"))
        assertTrue(formatted.contains("%"))
    }
}

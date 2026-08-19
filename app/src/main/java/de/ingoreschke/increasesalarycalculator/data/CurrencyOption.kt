package de.ingoreschke.increasesalarycalculator.data

import java.util.Currency
import java.util.Locale

data class CurrencyOption(
    val code: String,
    val displayName: String,
    val symbol: String,
    val locale: Locale? = null
) {
    companion object {
        const val CODE_AUTO = "AUTO"
        const val CODE_EUR = "EUR"
        const val CODE_USD = "USD"
        const val CODE_GBP = "GBP"
        const val CODE_CHF = "CHF"
        const val CODE_JPY = "JPY"
        const val CODE_CAD = "CAD"
        const val CODE_AUD = "AUD"
        const val CODE_PLN = "PLN"
        const val CODE_SEK = "SEK"

        fun getAll(deviceLocale: Locale): List<CurrencyOption> {
            val deviceCurrencySymbol = try {
                Currency.getInstance(deviceLocale).symbol
            } catch (_: Exception) {
                "€"
            }
            val deviceCurrencyCode = try {
                Currency.getInstance(deviceLocale).currencyCode
            } catch (_: Exception) {
                "EUR"
            }

            return listOf(
                CurrencyOption(CODE_AUTO, "System ($deviceCurrencyCode)", deviceCurrencySymbol, deviceLocale),
                CurrencyOption(CODE_EUR, "Euro (EUR)", "€", Locale.GERMANY),
                CurrencyOption(CODE_USD, "US Dollar (USD)", "$", Locale.US),
                CurrencyOption(CODE_GBP, "British Pound (GBP)", "£", Locale.UK),
                CurrencyOption(CODE_CHF, "Swiss Franc (CHF)", "CHF", Locale.forLanguageTag("de-CH")),
                CurrencyOption(CODE_JPY, "Japanese Yen (JPY)", "¥", Locale.JAPAN),
                CurrencyOption(CODE_CAD, "Canadian Dollar (CAD)", "CA$", Locale.CANADA),
                CurrencyOption(CODE_AUD, "Australian Dollar (AUD)", "AU$", Locale.forLanguageTag("en-AU")),
                CurrencyOption(CODE_PLN, "Polish Złoty (PLN)", "zł", Locale.forLanguageTag("pl-PL")),
                CurrencyOption(CODE_SEK, "Swedish Krona (SEK)", "kr", Locale.forLanguageTag("sv-SE"))
            )
        }

        fun getByCode(code: String, deviceLocale: Locale): CurrencyOption {
            val all = getAll(deviceLocale)
            return all.find { it.code.equals(code, ignoreCase = true) } ?: all.first()
        }
    }
}

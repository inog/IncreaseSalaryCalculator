package de.ingoreschke.increasesalarycalculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale


class SettingsViewModel : ViewModel() {
    private val _currency = MutableStateFlow(Currency.getInstance(Locale.getDefault()))
    val currency: StateFlow<Currency> = _currency.asStateFlow()

    private val _symbolAfterAmount = MutableStateFlow(true)
    val symbolAfterAmount: StateFlow<Boolean> = _symbolAfterAmount.asStateFlow()

    fun setCurrency(currency: Currency) {
        _currency.value = currency
    }

    fun setSymbolPosition(afterAmount: Boolean) {
        _symbolAfterAmount.value = afterAmount
    }

    fun formatCurrency(amount: Double): String {
        val numberFormat = NumberFormat.getCurrencyInstance()
        numberFormat.currency = _currency.value
        val formatted = numberFormat.format(amount)
        return if (_symbolAfterAmount.value) {
            formatted.replace(_currency.value.symbol, "") + " " + _currency.value.symbol
        } else {
            formatted
        }
    }
}

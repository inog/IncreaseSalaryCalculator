package de.ingoreschke.increasesalarycalculator

// SettingsScreen.kt

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.ingoreschke.increasesalarycalculator.ui.theme.IncreaseSalaryCalculatorTheme
import java.util.Currency
import java.util.Locale

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    var selectedCurrency by remember { mutableStateOf(Currency.getInstance(Locale.getDefault())) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Einstellungen", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Währung", style = MaterialTheme.typography.headlineSmall)

        Log.i(TAG, "SettingsScreen: ${Currency.getAvailableCurrencies()}")
        Currency.getAvailableCurrencies().forEach { currency ->
            Text("${currency.currencyCode} (${currency.symbol})", style = MaterialTheme.typography.bodyMedium)
        }

        DropdownMenu(
            expanded = false, // Steuerung des Dropdown-Menüs
            onDismissRequest = { /* Schließen-Logik */ }
        ) {
            Currency.getAvailableCurrencies().forEach { currency ->
                DropdownMenuItem(onClick = {
                    selectedCurrency = currency
                    viewModel.setCurrency(currency)
                }) {
                    Text("${currency.currencyCode} (${currency.symbol})")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Aktuelles Währungssymbol: ${selectedCurrency.symbol}")

        Spacer(modifier = Modifier.height(8.dp))

        // Option für die Position des Währungssymbols
        var symbolAfterAmount by remember { mutableStateOf(true) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Währungssymbol nach dem Betrag")
            Switch(
                checked = symbolAfterAmount,
                onCheckedChange = {
                    symbolAfterAmount = it
                    viewModel.setSymbolPosition(it)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Beispielanzeige
        val amount = 1234.56
        val formattedAmount = viewModel.formatCurrency(amount)
        Text("Beispiel: $formattedAmount")
    }
}

@Composable
fun DropdownMenuItem(onClick: () -> Unit, interactionSource: @Composable () -> Unit) {

}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    IncreaseSalaryCalculatorTheme {
        SettingsScreen(SettingsViewModel())
    }
}

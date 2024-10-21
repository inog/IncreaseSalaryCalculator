package de.ingoreschke.increasesalarycalculator


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
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

        Log.i("SettingsScreen", "SettingsScreen: ${Currency.getAvailableCurrencies()}")

        var cur = Currency.getAvailableCurrencies()
            .filter { it.currencyCode in listOf("EUR", "USD", "JPY") }
            .sortedBy { it.currencyCode }

        cur.forEach { currency ->
            Text("${currency.currencyCode} (${currency.symbol})", style = MaterialTheme.typography.bodyMedium)
        }

        var expanded by remember { mutableStateOf(false) }
        var mTextFieldSize by remember { mutableStateOf(Size.Zero) }
        OutlinedTextField(
            value = selectedCurrency.currencyCode,
            onValueChange = { selectedCurrency = Currency.getInstance(it) },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = { Text("Label") }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    mTextFieldSize = coordinates.size.toSize()
                }
        ) {
            cur.forEach { currency ->
                DropdownMenuItem(onClick = {
                    selectedCurrency = currency
                    viewModel.setCurrency(currency)
                    expanded = false
                }) {
                    Text("${currency.currencyCode} (${currency.symbol})")
                }
            }
        }

        // Ein Button oder ein anderes UI-Element, um das Dropdown-Menü zu öffnen
        Button(onClick = { expanded = true }) {
            Text("Währung auswählen")
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

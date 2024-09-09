package de.ingoreschke.increasesalarycalculator

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import kotlin.math.roundToInt

val NUMBER_REGEX = "-?\\d*[.]?\\d*".toRegex()

/**
 * Main screen of the app
 */
@Composable
fun SalaryIncrease(
    modifier: Modifier = Modifier,
    presenter: SalaryIncreasePresenter,
    lastSalary: BigDecimal,
    lastIncrease: BigDecimal
) {
    var currentSalary by remember { mutableStateOf(lastSalary) }
    var increasePercentage by remember { mutableStateOf(lastIncrease) }
    var result by remember { mutableStateOf(currentSalary) }

    val ctx = LocalContext.current
    val currencyFormat = remember(ctx) {
        NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance(ctx.resources.configuration.locales[0])
            isGroupingUsed = true
        }
    }

    Column(modifier = modifier) {
        SalaryInput(
            value = currentSalary.toString(),
            onValueChange = {
                currentSalary = it.toBigDecimalOrNull() ?: BigDecimal.ZERO
                result = presenter.calculateSalaryIncrease(currentSalary, increasePercentage)
                saveToPrefs(ctx, "last_salary", currentSalary.toString())
            }, modifier = modifier
        )

        IncreaseInput(
            value = increasePercentage.toString(),
            onValueChange = {
                increasePercentage = it.toBigDecimalOrNull() ?: BigDecimal.ZERO
                result = presenter.calculateSalaryIncrease(currentSalary, increasePercentage)
                saveToPrefs(ctx, "last_increase", increasePercentage.toString())
            },
            modifier = modifier
        )

        IncreaseSlider(
            value = increasePercentage.toFloat(),
            onValueChange = {
                increasePercentage = it.toBigDecimal().setScale(2)
                result = presenter.calculateSalaryIncrease(currentSalary, increasePercentage)
                saveToPrefs(ctx, "last_increase", increasePercentage.toString())
            },
            modifier = modifier
        )

        Spacer(modifier = modifier.height(16.dp))

        Text(
            text = currencyFormat.format(result),
            fontSize = 48.sp,
            modifier = modifier.heightIn(min = 48.dp),
        )
    }
}

/**
 * Input field for the salary
 */
@Composable
fun SalaryInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text("Current Salary") },
        trailingIcon = { CurrencySymbol() },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier
    )
}

@Composable
fun CurrencySymbol() {
    val ctx = LocalContext.current
    val currencySymbol = remember(ctx) {
        Currency.getInstance(ctx.resources.configuration.locales[0]).symbol
    }
    Text(currencySymbol)
}

/**
 * Input field for the increase
 */
@Composable
fun IncreaseInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text("Increase") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        trailingIcon = { Text("%") },
        isError = value.isNotEmpty() && !NUMBER_REGEX.matches(value),
        modifier = modifier
    )
}

/**
 * Slider to adjust the increase
 */
@Composable
fun IncreaseSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Slider(
        value = value,
        valueRange = 0f..20f,
        onValueChange = {
            val rounded = (it * 10).roundToInt().toFloat() / 10
            onValueChange(rounded)
        },
        modifier = modifier.height(96.dp),
    )
}

/**
 * Save a value to shared preferences
 */
fun saveToPrefs(context: Context, key: String, value: String) {
    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        .edit()
        .putString(key, value)
        .apply()
}

package de.ingoreschke.increasesalarycalculator

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import kotlin.math.roundToInt

val NUMBER_REGEX = "-?\\d*[.]?\\d*".toRegex()

@Composable
fun SalaryIncrease(modifier: Modifier = Modifier, presenter: SalaryIncreasePresenter, lastSalary: BigDecimal, lastIncrease: BigDecimal) {
    var currentSalary by remember { mutableStateOf(lastSalary) }
    var increasePercentage by remember { mutableStateOf(lastIncrease) }
    var result by remember { mutableStateOf(currentSalary) }
    val ctx = LocalContext.current
    val currentLocale = ctx.resources.configuration.locales[0]
    val numberFormat = NumberFormat.getCurrencyInstance(currentLocale)
    numberFormat.isGroupingUsed = true

    Log.i(TAG, "NumberFormat: " + numberFormat.currency)

    Column(modifier = modifier) {
        SalaryInput(
            value = currentSalary.toString(),
            onValueChange = {
                currentSalary = BigDecimal(it)
                result = presenter.calculateSalaryIncrease(currentSalary, increasePercentage)
                saveToPrefs(ctx, "last_salary", currentSalary.toString())
            }, modifier = modifier
        )

        IncreaseInput(
            value = increasePercentage.toString(),
            onValueChange = {
                increasePercentage = BigDecimal(it)
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
            text = "${numberFormat.format(result)}",
            fontSize = 48.sp,
            modifier = modifier.heightIn(min = 48.dp),
        )
    }
}

@Composable
fun SalaryInput(modifier: Modifier = Modifier,
                value: String,
                onValueChange: (String) -> Unit) {

    TextField(
        value = value,
        onValueChange = {
            if (it.isEmpty() || NUMBER_REGEX.matches(it)) {
                onValueChange(if (it.isEmpty()) "0" else it)
            }
        },
        label = { Text("Current Salary") },
        trailingIcon = { Text("€") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier
    )
}

@Composable
fun IncreaseInput(modifier: Modifier = Modifier,
                  value: String,
                  onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = {
            if (it.isEmpty() || NUMBER_REGEX.matches(it)) {
                onValueChange(if (it.isEmpty()) "0" else it)
            }
        },
        label = { Text("Increase") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        //placeholder = { Text("5.4") },
        trailingIcon = { Text("%") },
        isError = value.isNotEmpty() && !NUMBER_REGEX.matches(value),
        modifier = modifier
    )
}

@Composable
fun IncreaseSlider(modifier: Modifier = Modifier,
                   value: Float,
                   onValueChange: (Float) -> Unit) {
    Slider(
        value = value.toFloat(),
        valueRange = 0f..20f,
        onValueChange = {
            val rounded = (it * 10).roundToInt().toFloat() / 10
            onValueChange(rounded )
        }, modifier = modifier.height(96.dp),
    )
}

fun saveToPrefs(context: Context, key: String, value: String) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putString(key, value)
    editor.apply()
}

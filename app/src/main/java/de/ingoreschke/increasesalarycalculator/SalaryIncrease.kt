package de.ingoreschke.increasesalarycalculator

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.ingoreschke.increasesalarycalculator.domain.SalaryCalculator
import java.math.BigDecimal

val NUMBER_REGEX = "^-?[0-9]*[.,]?[0-9]*$".toRegex()

fun saveToPrefs(context: Context, key: String, value: String) {
    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        .edit()
        .putString(key, value)
        .apply()
}

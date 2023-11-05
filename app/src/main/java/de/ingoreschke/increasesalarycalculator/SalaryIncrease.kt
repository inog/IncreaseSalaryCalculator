package de.ingoreschke.increasesalarycalculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SalaryIncrease(modifier: Modifier = Modifier, presenter: SalaryIncreasePresenter) {
    var currentSalary by remember { mutableStateOf(65000.00) }
    var increasePercentage by remember { mutableStateOf(0.0) }
    var result by remember { mutableStateOf(0.0) }

    Column(modifier = modifier) {
        TextField(
            value = currentSalary.toString(),
            onValueChange = {
                currentSalary = it.toDouble()
                result = presenter.calculateSalaryIncrease(currentSalary, increasePercentage)
            },
            label = { Text("Current Salary") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier
        )

        TextField(
            value = increasePercentage.toString(),
            onValueChange = {
                increasePercentage = it.toDouble()
                result = presenter.calculateSalaryIncrease(currentSalary, increasePercentage)
            },
            label = { Text("Increase") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier
        )

        Text(text = result.toString(), modifier = modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun SalaryIncreasePreview() {
    SalaryIncrease(presenter = SalaryIncreasePresenter(SalaryIncreaseInteractor()))
}

@Composable
fun CurrencyInput(modifier: Modifier = Modifier, value: Double, onValueChange: (Double) -> Unit) {
    TextField(
        value = value.toString(),
        onValueChange = {
            onValueChange(it.toDouble())
        },
        label = { Text("Current Salary") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}
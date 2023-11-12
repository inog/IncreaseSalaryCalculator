package de.ingoreschke.increasesalarycalculator

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SalaryIncrease(modifier: Modifier = Modifier, presenter: SalaryIncreasePresenter) {
    var currentSalary by remember { mutableStateOf(65000.00) }
    var increasePercentage by remember { mutableStateOf(0.0) }
    var result by remember { mutableStateOf(currentSalary) }


    Column(modifier = modifier
        .padding(16.dp)) {
        SalaryInput(
            value = currentSalary,
            onValueChange = {
                currentSalary = it
                result = presenter.calculateSalaryIncrease(currentSalary, increasePercentage)
            }, modifier = modifier
        )

        IncreaseInput(
            value = increasePercentage,
            onValueChange = {
                increasePercentage = it
                result = presenter.calculateSalaryIncrease(currentSalary, increasePercentage)
            },
            modifier = modifier
        )

        IncreaseSlider(
            value = increasePercentage,
            onValueChange = {
                increasePercentage = it
                result = presenter.calculateSalaryIncrease(currentSalary, increasePercentage)
            },
            modifier = modifier
        )

        Spacer(modifier = modifier.height(16.dp))

        Text(
            text = "$result €",
            fontSize = 48.sp,
            modifier = modifier.heightIn(min = 48.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SalaryIncreasePreview() {
    SalaryIncrease(presenter = SalaryIncreasePresenter(SalaryIncreaseInteractor()))
}

@Composable
fun SalaryInput(modifier: Modifier = Modifier, value: Double, onValueChange: (Double) -> Unit) {
    TextField(
        value = value.toString(),
        onValueChange = {
            onValueChange(it.toDouble())
        },
        label = { Text("Current Salary") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        trailingIcon = { Text("€") } ,
        modifier = modifier
    )
}

@Composable
fun IncreaseInput(modifier: Modifier = Modifier, value: Double, onValueChange: (Double) -> Unit) {
    TextField(
        value = value.toString(),
        onValueChange = {
            onValueChange(it.toDouble())
        },
        label = { Text("Increase") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        trailingIcon = { Text("%") } ,
        modifier = modifier
    )
}

@Composable
fun IncreaseSlider(modifier: Modifier = Modifier, value: Double, onValueChange: (Double) -> Unit) {
    Slider(
        value = value.toFloat(),
        valueRange = 0f..20f,
        onValueChange = {
            onValueChange(it.toDouble())
        }, modifier = modifier.height(96.dp)
    )
}

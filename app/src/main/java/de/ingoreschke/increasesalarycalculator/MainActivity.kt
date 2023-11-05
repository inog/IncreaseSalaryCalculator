package de.ingoreschke.increasesalarycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import de.ingoreschke.increasesalarycalculator.ui.theme.IncreaseSalaryCalculatorTheme

class MainActivity : ComponentActivity() {
    private lateinit var presenter: SalaryIncreasePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SalaryIncreasePresenter(SalaryIncreaseInteractor())

        setContent {
            IncreaseSalaryCalculatorTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                    CurrentSalary(presenter = presenter)
                }
            }
        }
    }
}

@Composable
fun CurrentSalary(modifier: Modifier = Modifier, presenter: SalaryIncreasePresenter) {
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

        Text(text = result.toString())
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IncreaseSalaryCalculatorTheme {
        Greeting("Android")
        CurrentSalary(presenter = SalaryIncreasePresenter(SalaryIncreaseInteractor()))
    }
}
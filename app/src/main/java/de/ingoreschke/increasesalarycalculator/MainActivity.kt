package de.ingoreschke.increasesalarycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                    SalaryIncrease(presenter = presenter)
                }
            }
        }
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
        Column {
            Greeting("Android")
            SalaryIncrease(presenter = SalaryIncreasePresenter(SalaryIncreaseInteractor()))
        }
    }
}
package de.ingoreschke.increasesalarycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.google.android.gms.ads.MobileAds
import de.ingoreschke.increasesalarycalculator.data.DataStoreSalaryPreferencesRepository
import de.ingoreschke.increasesalarycalculator.ui.SalaryScreen
import de.ingoreschke.increasesalarycalculator.ui.SalaryViewModel
import de.ingoreschke.increasesalarycalculator.ui.components.ConsentManager
import de.ingoreschke.increasesalarycalculator.ui.theme.IncreaseSalaryCalculatorTheme

class MainActivity : ComponentActivity() {

    private val viewModel: SalaryViewModel by viewModels {
        SalaryViewModel.provideFactory(DataStoreSalaryPreferencesRepository(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize Google Mobile Ads and UMP Consent
        ConsentManager.requestConsent(this) {
            MobileAds.initialize(this) {}
        }

        setContent {
            IncreaseSalaryCalculatorTheme {
                SalaryScreen(viewModel = viewModel)
            }
        }
    }
}

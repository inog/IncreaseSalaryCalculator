package de.ingoreschke.increasesalarycalculator

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.ingoreschke.increasesalarycalculator.data.CalculationMode
import de.ingoreschke.increasesalarycalculator.data.SalaryPeriod
import de.ingoreschke.increasesalarycalculator.data.SalaryPreferencesRepository
import de.ingoreschke.increasesalarycalculator.data.SalaryUserPreferences
import de.ingoreschke.increasesalarycalculator.ui.SalaryScreen
import de.ingoreschke.increasesalarycalculator.ui.SalaryViewModel
import de.ingoreschke.increasesalarycalculator.ui.theme.IncreaseSalaryCalculatorTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SalaryScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun salaryScreen_rendersKeyComponents() {
        val fakeRepo = object : SalaryPreferencesRepository {
            private val flow = MutableStateFlow(SalaryUserPreferences())
            override val userPreferencesFlow: Flow<SalaryUserPreferences> = flow
            override suspend fun updateSalary(salary: String) {}
            override suspend fun updatePercentage(percentage: String) {}
            override suspend fun updateTargetSalary(targetSalary: String) {}
            override suspend fun updatePeriod(period: SalaryPeriod) {}
            override suspend fun updateMode(mode: CalculationMode) {}
            override suspend fun reset() {}
        }
        val viewModel = SalaryViewModel(fakeRepo)

        composeTestRule.setContent {
            IncreaseSalaryCalculatorTheme {
                SalaryScreen(viewModel = viewModel)
            }
        }

        // Verify chips and preset buttons are rendered
        composeTestRule.onNodeWithText("+5%").assertIsDisplayed()
        composeTestRule.onNodeWithText("+10%").assertIsDisplayed()

        // Click on preset chip
        composeTestRule.onNodeWithText("+10%").performClick()
    }
}

package de.ingoreschke.increasesalarycalculator

import de.ingoreschke.increasesalarycalculator.data.CalculationMode
import de.ingoreschke.increasesalarycalculator.data.SalaryPeriod
import de.ingoreschke.increasesalarycalculator.data.SalaryPreferencesRepository
import de.ingoreschke.increasesalarycalculator.data.SalaryUserPreferences
import de.ingoreschke.increasesalarycalculator.ui.SalaryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class SalaryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeSalaryPreferencesRepository
    private lateinit var viewModel: SalaryViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeSalaryPreferencesRepository()
        viewModel = SalaryViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadPreferences_initializesStateFromRepository() = runTest {
        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertTrue(state.isLoaded)
        assertEquals("3500.00", state.salaryInput)
        assertEquals("5.0", state.percentageInput)
        assertEquals(5.0f, state.sliderPercentage)
        assertEquals(SalaryPeriod.MONTHLY, state.selectedPeriod)
        assertEquals(CalculationMode.PERCENTAGE, state.calculationMode)
        assertNotNull(state.percentageResult)
        assertEquals(BigDecimal("3675.00"), state.percentageResult?.newSalary)
    }

    @Test
    fun onSalaryChanged_updatesSalaryAndCalculations() = runTest {
        advanceUntilIdle()
        viewModel.onSalaryChanged("4000")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("4000", state.salaryInput)
        assertTrue(state.isSalaryValid)
        assertEquals(BigDecimal("4200.00"), state.percentageResult?.newSalary)
        assertEquals(BigDecimal("200.00"), state.percentageResult?.difference)
    }

    @Test
    fun onPercentageChanged_updatesPercentageAndSlider() = runTest {
        advanceUntilIdle()
        viewModel.onPercentageChanged("10.0")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("10.0", state.percentageInput)
        assertEquals(10.0f, state.sliderPercentage)
        assertEquals(BigDecimal("3850.00"), state.percentageResult?.newSalary)
    }

    @Test
    fun onSliderPercentageChanged_updatesStateCorrectly() = runTest {
        advanceUntilIdle()
        viewModel.onSliderPercentageChanged(7.5f)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("7.5", state.percentageInput)
        assertEquals(7.5f, state.sliderPercentage)
        assertEquals(BigDecimal("3762.50"), state.percentageResult?.newSalary)
    }

    @Test
    fun onPresetSelected_updatesPercentage() = runTest {
        advanceUntilIdle()
        viewModel.onPresetSelected(15.0)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("15", state.percentageInput)
        assertEquals(15.0f, state.sliderPercentage)
    }

    @Test
    fun onTargetSalaryChanged_calculatesRequiredPercentage() = runTest {
        advanceUntilIdle()
        viewModel.onTargetSalaryChanged("4200")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("4200", state.targetSalaryInput)
        assertEquals(BigDecimal("20.00"), state.targetResult?.requiredPercentage)
        assertEquals(BigDecimal("700.00"), state.targetResult?.difference)
    }

    @Test
    fun onPeriodChanged_updatesPeriodAndRecalculates() = runTest {
        advanceUntilIdle()
        viewModel.onPeriodChanged(SalaryPeriod.ANNUAL)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(SalaryPeriod.ANNUAL, state.selectedPeriod)
        assertEquals(SalaryPeriod.ANNUAL, state.percentageResult?.period)
    }

    @Test
    fun onModeChanged_switchesMode() = runTest {
        advanceUntilIdle()
        viewModel.onModeChanged(CalculationMode.TARGET_SALARY)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(CalculationMode.TARGET_SALARY, state.calculationMode)
    }

    @Test
    fun onReset_clearsState() = runTest {
        advanceUntilIdle()
        viewModel.onReset()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("", state.salaryInput)
        assertEquals("", state.percentageInput)
        assertEquals(0f, state.sliderPercentage)
    }

    @Test
    fun toggleInfoDialog_updatesDialogVisibility() = runTest {
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.showInfoDialog)

        viewModel.toggleInfoDialog(true)
        assertTrue(viewModel.uiState.value.showInfoDialog)

        viewModel.toggleInfoDialog(false)
        assertFalse(viewModel.uiState.value.showInfoDialog)
    }
}

class FakeSalaryPreferencesRepository : SalaryPreferencesRepository {
    private val preferencesFlow = MutableStateFlow(SalaryUserPreferences())

    override val userPreferencesFlow: Flow<SalaryUserPreferences> = preferencesFlow

    override suspend fun updateSalary(salary: String) {
        preferencesFlow.value = preferencesFlow.value.copy(lastSalary = salary)
    }

    override suspend fun updatePercentage(percentage: String) {
        preferencesFlow.value = preferencesFlow.value.copy(lastPercentage = percentage)
    }

    override suspend fun updateTargetSalary(targetSalary: String) {
        preferencesFlow.value = preferencesFlow.value.copy(lastTargetSalary = targetSalary)
    }

    override suspend fun updatePeriod(period: SalaryPeriod) {
        preferencesFlow.value = preferencesFlow.value.copy(period = period)
    }

    override suspend fun updateMode(mode: CalculationMode) {
        preferencesFlow.value = preferencesFlow.value.copy(mode = mode)
    }

    override suspend fun reset() {
        preferencesFlow.value = SalaryUserPreferences(
            lastSalary = "0.0",
            lastPercentage = "0.0",
            lastTargetSalary = "0.0"
        )
    }
}

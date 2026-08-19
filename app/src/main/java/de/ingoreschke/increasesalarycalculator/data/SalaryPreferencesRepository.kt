package de.ingoreschke.increasesalarycalculator.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.salaryDataStore: DataStore<Preferences> by preferencesDataStore(name = "salary_settings")

interface SalaryPreferencesRepository {
    val userPreferencesFlow: Flow<SalaryUserPreferences>
    suspend fun updateSalary(salary: String)
    suspend fun updatePercentage(percentage: String)
    suspend fun updateTargetSalary(targetSalary: String)
    suspend fun updatePeriod(period: SalaryPeriod)
    suspend fun updateMode(mode: CalculationMode)
    suspend fun updateCurrency(currencyCode: String)
    suspend fun reset()
}

data class SalaryUserPreferences(
    val lastSalary: String = "3500.00",
    val lastPercentage: String = "5.0",
    val lastTargetSalary: String = "3850.00",
    val period: SalaryPeriod = SalaryPeriod.MONTHLY,
    val mode: CalculationMode = CalculationMode.PERCENTAGE,
    val currencyCode: String = CurrencyOption.CODE_AUTO
)

class DataStoreSalaryPreferencesRepository(
    private val context: Context
) : SalaryPreferencesRepository {

    private object PreferencesKeys {
        val LAST_SALARY = stringPreferencesKey("last_salary")
        val LAST_PERCENTAGE = stringPreferencesKey("last_percentage")
        val LAST_TARGET_SALARY = stringPreferencesKey("last_target_salary")
        val PERIOD = stringPreferencesKey("salary_period")
        val MODE = stringPreferencesKey("calculation_mode")
        val CURRENCY = stringPreferencesKey("selected_currency")
    }

    override val userPreferencesFlow: Flow<SalaryUserPreferences> =
        context.salaryDataStore.data.map { preferences ->
            val legacyPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val legacySalary = legacyPrefs.getString("last_salary", null)
            val legacyIncrease = legacyPrefs.getString("last_increase", null)

            val salary = preferences[PreferencesKeys.LAST_SALARY] 
                ?: legacySalary 
                ?: "3500.00"
            val percentage = preferences[PreferencesKeys.LAST_PERCENTAGE] 
                ?: legacyIncrease 
                ?: "5.0"
            val targetSalary = preferences[PreferencesKeys.LAST_TARGET_SALARY] ?: "3850.00"
            
            val period = preferences[PreferencesKeys.PERIOD]?.let {
                try { SalaryPeriod.valueOf(it) } catch (_: Exception) { SalaryPeriod.MONTHLY }
            } ?: SalaryPeriod.MONTHLY

            val mode = preferences[PreferencesKeys.MODE]?.let {
                try { CalculationMode.valueOf(it) } catch (_: Exception) { CalculationMode.PERCENTAGE }
            } ?: CalculationMode.PERCENTAGE

            val currency = preferences[PreferencesKeys.CURRENCY] ?: CurrencyOption.CODE_AUTO

            SalaryUserPreferences(
                lastSalary = salary,
                lastPercentage = percentage,
                lastTargetSalary = targetSalary,
                period = period,
                mode = mode,
                currencyCode = currency
            )
        }

    override suspend fun updateSalary(salary: String) {
        context.salaryDataStore.edit { it[PreferencesKeys.LAST_SALARY] = salary }
    }

    override suspend fun updatePercentage(percentage: String) {
        context.salaryDataStore.edit { it[PreferencesKeys.LAST_PERCENTAGE] = percentage }
    }

    override suspend fun updateTargetSalary(targetSalary: String) {
        context.salaryDataStore.edit { it[PreferencesKeys.LAST_TARGET_SALARY] = targetSalary }
    }

    override suspend fun updatePeriod(period: SalaryPeriod) {
        context.salaryDataStore.edit { it[PreferencesKeys.PERIOD] = period.name }
    }

    override suspend fun updateMode(mode: CalculationMode) {
        context.salaryDataStore.edit { it[PreferencesKeys.MODE] = mode.name }
    }

    override suspend fun updateCurrency(currencyCode: String) {
        context.salaryDataStore.edit { it[PreferencesKeys.CURRENCY] = currencyCode }
    }

    override suspend fun reset() {
        context.salaryDataStore.edit {
            it[PreferencesKeys.LAST_SALARY] = "0.0"
            it[PreferencesKeys.LAST_PERCENTAGE] = "0.0"
            it[PreferencesKeys.LAST_TARGET_SALARY] = "0.0"
            it[PreferencesKeys.PERIOD] = SalaryPeriod.MONTHLY.name
            it[PreferencesKeys.MODE] = CalculationMode.PERCENTAGE.name
            it[PreferencesKeys.CURRENCY] = CurrencyOption.CODE_AUTO
        }
    }
}

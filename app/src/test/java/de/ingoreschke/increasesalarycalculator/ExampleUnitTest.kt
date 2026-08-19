package de.ingoreschke.increasesalarycalculator

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class ExampleUnitTest {

    @Test
    fun legacyPresenterAndInteractor_calculateCorrectly() {
        val interactor = SalaryIncreaseInteractor()
        val presenter = SalaryIncreasePresenter(interactor)

        val result = presenter.calculateSalaryIncrease(BigDecimal("3000.00"), BigDecimal("10.0"))
        assertEquals(BigDecimal("3300.00"), result)
    }
}

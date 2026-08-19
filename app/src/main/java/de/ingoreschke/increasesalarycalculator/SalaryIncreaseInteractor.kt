package de.ingoreschke.increasesalarycalculator

import de.ingoreschke.increasesalarycalculator.domain.SalaryCalculator
import java.math.BigDecimal

class SalaryIncreaseInteractor {
    fun calcIncreasedSalary(startSalary: BigDecimal, increasePercentage: BigDecimal): BigDecimal {
        return SalaryCalculator.calculateIncrease(startSalary, increasePercentage).newSalary
    }
}

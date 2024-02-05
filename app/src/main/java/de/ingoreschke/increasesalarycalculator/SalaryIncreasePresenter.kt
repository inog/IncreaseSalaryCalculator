package de.ingoreschke.increasesalarycalculator

import java.math.BigDecimal

class SalaryIncreasePresenter(private val salaryIncreaseInteractor: SalaryIncreaseInteractor) {

    fun calculateSalaryIncrease(salary: BigDecimal, increasePercentage: BigDecimal): BigDecimal {
        return salaryIncreaseInteractor.calcIncreasedSalary(salary, increasePercentage)
    }
}
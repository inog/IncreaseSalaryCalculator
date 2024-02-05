package de.ingoreschke.increasesalarycalculator

import java.math.BigDecimal
import java.math.RoundingMode

class SalaryIncreaseInteractor {

    fun calcIncreasedSalary(startSalary: BigDecimal, increasePercentage: BigDecimal): BigDecimal {
        val increasedSalary = startSalary.add(startSalary.multiply(increasePercentage).divide(BigDecimal(100)))
        return increasedSalary.setScale(2, RoundingMode.HALF_UP)
    }
}



package de.ingoreschke.increasesalarycalculator

private const val HUNDRED = 100.0

class SalaryIncreaseInteractor {

    fun calcIncreasedSalary(startSalary: Double, increasePercentage: Double): Double {
        var salaryIncrease = startSalary + (startSalary * increasePercentage / HUNDRED)
        salaryIncrease = Math.round(salaryIncrease * HUNDRED) / HUNDRED
        return salaryIncrease
    }

}

